package coli.babbang.service;

import coli.babbang.client.GithubClient;
import coli.babbang.domain.github.ReviewStatus;
import coli.babbang.domain.github.Reviewers;
import coli.babbang.domain.notifier.DiscordNotifier;
import coli.babbang.domain.notifier.DiscordProperty;
import coli.babbang.domain.github.GithubPullRequest;
import coli.babbang.domain.github.GithubRepo;
import coli.babbang.domain.github.GithubRepoUrl;
import coli.babbang.domain.reminder.RemindMessageResolver;
import coli.babbang.domain.reminder.ReminderInfo;
import coli.babbang.domain.reminder.ReminderType;
import coli.babbang.domain.github.Reviewer;
import coli.babbang.dto.request.ReminderCreateRequest;
import coli.babbang.dto.response.GithubPullRequestReviewResponse;
import coli.babbang.dto.response.GithubRepoInfoResponse;
import coli.babbang.exception.custom.BabbangException;
import coli.babbang.exception.errorcode.ErrorCode;
import coli.babbang.repository.DiscordPropertyRepository;
import coli.babbang.repository.GithubRepoRepository;
import coli.babbang.repository.PullRequestRepository;
import coli.babbang.repository.ReminderRepository;
import coli.babbang.repository.ReviewerRepository;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class ReminderService {

    @Value("${github.token}")
    private String masterToken;

    @Value("${webhook.url}")
    private String webhookUrl;

    private final GithubClient githubClient;
    private final GithubRepoRepository githubRepoRepository;
    private final ReviewerRepository reviewerRepository;
    private final ReminderRepository reminderRepository;
    private final DiscordPropertyRepository discordPropertyRepository;
    private final PullRequestRepository pullRequestRepository;
    private final RemindMessageResolver remindMessageResolver;
    private final DiscordNotifier discordNotifier;
    private final TaskScheduler taskScheduler;
    private final TransactionTemplate transactionTemplate;

    @Transactional
    public void create(ReminderCreateRequest request) {
        //레포 저장 -> 리뷰어 저장 -> 리마인더 저장 -> 디스코드 채널 속성 생성 -> 웹훅 생성
        GithubRepoUrl githubRepoUrl = new GithubRepoUrl(request.githubRepoUrl());
        GithubRepoInfoResponse repoInfos = githubClient.getRepoInfos(githubRepoUrl, masterToken);
        GithubRepo githubRepo = new GithubRepo(repoInfos.id(), request.githubRepoUrl());
        GithubRepo savedRepo = githubRepoRepository.save(githubRepo);

        //리뷰어 저장
        Reviewers reviewers = new Reviewers(request.reviewers(), savedRepo.getId());
        reviewerRepository.saveAll(reviewers.getValues());

        //리마인더 저장
        ReminderInfo reminderInfo = new ReminderInfo(request.approveCount(), savedRepo.getId(), request.reviewToHour());
        reminderRepository.save(reminderInfo);

        //디스코드 속성 생성
        DiscordProperty discordProperty = new DiscordProperty(savedRepo.getId(), request.channelId(), request.discordBotToken());
        discordPropertyRepository.save(discordProperty);

        githubClient.registerWebhook(githubRepoUrl, webhookUrl, masterToken);
    }

    public void scheduleReminder(GithubPullRequest pullRequest) {
        ReminderInfo reminderInfo = reminderRepository.getByRepositoryId(pullRequest.getRepoId());
        long afterHour = reminderInfo.getReviewHour();
        Instant runAt = Instant.now().plusSeconds(afterHour * 3600);
        taskScheduler.schedule(() -> remindPullRequest(pullRequest.getId(), ReminderType.REMINDER), runAt);
    }

    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
    public void sendMorningReminder() {
        List<GithubPullRequest> waitingPullRequests = pullRequestRepository.findAllByStatus(ReviewStatus.WAITING);

        for (GithubPullRequest pullRequest : waitingPullRequests) {
            remindPullRequest(pullRequest.getId(), ReminderType.MORNING);
        }
    }

    public void remindPullRequest(long pullRequestId, ReminderType reminderType) {
        transactionTemplate.executeWithoutResult(transactionStatus-> {
            //풀리퀘 가져오기 -> 머지되었는지 확인, 머지안되었으면 리뷰정보 가져오기 -> 머지 안한 사람을 담아 리뷰 확인
            GithubPullRequest githubPullRequest = pullRequestRepository.getByAppId(pullRequestId);
            GithubRepo repo = githubRepoRepository.getByAppId(githubPullRequest.getRepoId());
            ReminderInfo reminderInfo = reminderRepository.getByRepositoryId(repo.getId());

            if (githubPullRequest.isMerged()) {
                return;
            }

            GithubPullRequestReviewResponse pullRequestInfo = githubClient.getPullRequestInfo(
                    repo.getGithubRepoUrl(),
                    githubPullRequest.getNumber(),
                    masterToken
            );

            if (reminderInfo.getApproveCount() <= pullRequestInfo.approveCount()) {
                githubPullRequest.merge();
                return;
            }

            githubPullRequest.reviewing();
            Set<String> alreadyReviewedReviewer = findDoneReviwewers(pullRequestInfo, githubPullRequest);
            List<Reviewer> notReviewedReviewers = reviewerRepository.findStupidReviewers(alreadyReviewedReviewer,
                    repo.getId());
            sendMessageToRepo(notReviewedReviewers, repo, githubPullRequest, reminderType);
        });
    }

    private void sendMessageToRepo(
            List<Reviewer> notReviewedReviewers,
            GithubRepo repo,
            GithubPullRequest pullRequest,
            ReminderType reminderType
    ) {
        String message = remindMessageResolver.resolve(notReviewedReviewers, reminderType, repo, pullRequest);
        DiscordProperty discordProperty = discordPropertyRepository.findByRepoId(repo.getId())
                .orElseThrow(() -> new BabbangException(ErrorCode.DISCORD_PROPERTY_NOT_FOUND));

        discordNotifier.sendMessage(message, discordProperty);
    }

    private Set<String> findDoneReviwewers(GithubPullRequestReviewResponse reviewInfo, GithubPullRequest pullRequest) {
        Set<String> alreadyReviewedReviewer = new HashSet<>();
        alreadyReviewedReviewer.add(pullRequest.getOpenUser());
        alreadyReviewedReviewer.addAll(reviewInfo.approveTeamMateNames());
        return alreadyReviewedReviewer;
    }
}
