package coli.babbang.service;

import coli.babbang.domain.GithubPullRequest;
import coli.babbang.domain.GithubRepo;
import coli.babbang.domain.ReviewStatus;
import coli.babbang.dto.request.GithubWebhookEventRequest;
import coli.babbang.repository.GithubRepoRepository;
import coli.babbang.repository.PullRequestRepository;
import coli.babbang.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GithubPullRequestService {

    private final ReminderService reminderService;
    private final PullRequestRepository pullRequestRepository;
    private final GithubRepoRepository githubRepoRepository;

    @Transactional
    public void handleWebhookEvent(GithubWebhookEventRequest request){
        //닫히는 이벤트 -> PR 업데이트
        if(request.isMerged()) {
            GithubPullRequest mergedPullRequest = pullRequestRepository.getByExternalId(request.getExternalId());
            mergedPullRequest.merge();
            return;
        }

        //열리는 이벤트 -> PR 저장 -> 리마인더 예약
        if(request.isOpened()) {
            GithubRepo githubRepo = githubRepoRepository.getByExternalId(request.getRepositoryId());
            GithubPullRequest openedPullRequest = new GithubPullRequest(
                    request.getExternalId(),
                    githubRepo.getId(),
                    request.getOpenUser(),
                    ReviewStatus.PENDING
            );
            GithubPullRequest pullRequest = pullRequestRepository.save(openedPullRequest);
            reminderService.scheduleReminder(pullRequest);
        }
    }
}
