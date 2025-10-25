package coli.babbang.service;

import coli.babbang.client.GithubClient;
import coli.babbang.domain.GithubRepo;
import coli.babbang.domain.GithubRepoUrl;
import coli.babbang.domain.Reviewer;
import coli.babbang.dto.request.ReminderCreateRequest;
import coli.babbang.dto.response.GithubRepoInfoResponse;
import coli.babbang.repository.GithubRepoRepository;
import coli.babbang.repository.ReviewerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final GithubClient githubClient;
    private final GithubRepoRepository githubRepoRepository;
    private final ReviewerRepository reviewerRepository;

    private String masterToken;

    public void create(ReminderCreateRequest request) {
        //레포 저장 -> 리뷰어 저장 -> 웹훅 생성
        GithubRepoUrl githubRepoUrl = new GithubRepoUrl(request.githubRepoUrl());
        GithubRepoInfoResponse repoInfos = githubClient.getRepoInfos(githubRepoUrl, masterToken);
        GithubRepo githubRepo = new GithubRepo(repoInfos.id(), request.githubRepoUrl());
        GithubRepo savedRepo = githubRepoRepository.save(githubRepo);

        //리뷰어 저장
        List<Reviewer> reviewers = request.reviewers().stream()
                .map(name -> new Reviewer(savedRepo.getId(), name))
                .toList();
        reviewerRepository.saveAll(reviewers);

        githubClient.registerWebhook(githubRepoUrl, "웹훅 Url", masterToken);
    }
}
