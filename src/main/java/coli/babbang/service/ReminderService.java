package coli.babbang.service;

import coli.babbang.client.GithubClient;
import coli.babbang.domain.notifier.DiscordNotifier;
import coli.babbang.domain.notifier.DiscordProperty;
import coli.babbang.domain.github.GithubPullRequest;
import coli.babbang.domain.github.GithubRepo;
import coli.babbang.domain.github.GithubRepoUrl;
import coli.babbang.domain.reminder.RemindMessageResolver;
import coli.babbang.domain.reminder.ReminderInfo;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReminderService {

    @Value("${github.token}")
    private String masterToken;
    private final GithubClient githubClient;
    private final GithubRepoRepository githubRepoRepository;
    private final ReviewerRepository reviewerRepository;
    private final ReminderRepository reminderRepository;
    private final DiscordPropertyRepository discordPropertyRepository;
    private final PullRequestRepository pullRequestRepository;
    private final RemindMessageResolver remindMessageResolver;
    private final DiscordNotifier discordNotifier;
    private final TaskScheduler taskScheduler;

    @Transactional
    public void create(ReminderCreateRequest request) {
        //레포 저장 -> 리뷰어 저장 -> 리마인더 저장 -> 디스코드 채널 속성 생성 -> 웹훅 생성
        GithubRepoUrl githubRepoUrl = new GithubRepoUrl(request.githubRepoUrl());
        GithubRepoInfoResponse repoInfos = githubClient.getRepoInfos(githubRepoUrl, masterToken);
        GithubRepo githubRepo = new GithubRepo(repoInfos.id(), request.githubRepoUrl());
        GithubRepo savedRepo = githubRepoRepository.save(githubRepo);

        //리뷰어 저장
        List<Reviewer> reviewers = request.reviewers().stream()
                .map(name -> new Reviewer(savedRepo.getId(), name))
                .toList();
        reviewerRepository.saveAll(reviewers);

        //리마인더 저장
        ReminderInfo reminderInfo = new ReminderInfo(request.approveCount(), savedRepo.getId(), request.reviewToHour());
        reminderRepository.save(reminderInfo);

        //디스코드 속성 생성
        DiscordProperty discordProperty = new DiscordProperty(savedRepo.getId(), request.channelId());
        discordPropertyRepository.save(discordProperty);

        githubClient.registerWebhook(githubRepoUrl, "웹훅 Url", masterToken);
    }

    public void scheduleReminder(GithubPullRequest pullRequest) {
        ReminderInfo reminderInfo = reminderRepository.getByRepositoryId(pullRequest.getRepoId());
        long afterHour = reminderInfo.getReviewHour();
        Instant runAt = Instant.now().plusSeconds(afterHour * 3600);
        taskScheduler.schedule(() -> remindPullRequest(pullRequest.getId()), runAt);
    }

    @Transactional
    public void remindPullRequest(long pullRequestId) {
        //풀리퀘 가져오기 -> 머지되었는지 확인, 머지안되었으면 리뷰정보 가져오기 -> 머지 안한 사람을 담아 리뷰 확인
        GithubPullRequest githubPullRequest = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new BabbangException(ErrorCode.PULL_REQUEST_NOT_FOUND));
        GithubRepo repo = githubRepoRepository.findById(githubPullRequest.getRepoId())
                .orElseThrow(() -> new BabbangException(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND));
        ReminderInfo reminderInfo = reminderRepository.getByRepositoryId(repo.getId());

        if (githubPullRequest.isMerged()) {
            return;
        }

        GithubPullRequestReviewResponse pullRequestInfo = githubClient.getPullRequestInfo(repo.getGithubRepoUrl(),
                githubPullRequest.getExternalId(), masterToken);

        if (reminderInfo.getApproveCount() <= pullRequestInfo.approveCount()) {
            githubPullRequest.merge();
            return;
        }
        Set<String> alreadyReviewedReviewer = new HashSet<>();
        alreadyReviewedReviewer.add(githubPullRequest.getOpenUser());
        alreadyReviewedReviewer.addAll(pullRequestInfo.approveTeamMateNames());
        githubPullRequest.reviewing();
        List<Reviewer> notReviewedReviewers = reviewerRepository.findAllByRepoId(repo.getId())
                .stream()
                .filter(reviewer -> !alreadyReviewedReviewer.contains(reviewer.getName()))
                .toList();

        String message = remindMessageResolver.resolve(notReviewedReviewers);

        DiscordProperty discordProperty = discordPropertyRepository.findByRepoId(repo.getId())
                .orElseThrow(() -> new BabbangException(ErrorCode.DISCORD_PROPERTY_NOT_FOUND));

        discordNotifier.sendMessage(message, discordProperty.getChannelId());
    }
}
