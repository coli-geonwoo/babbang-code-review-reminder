package coli.babbang.client;

import coli.babbang.domain.github.GithubRepoUrl;
import coli.babbang.dto.request.GithubWebhookRequest;
import coli.babbang.dto.response.GithubPullRequestReviewResponse;
import coli.babbang.dto.response.GithubRepoInfoResponse;
import coli.babbang.dto.response.GithubReviewResponse;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubClient {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";

    private final RestClient restClient;

    public GithubClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(GITHUB_API_BASE_URL)
                .build();
    }

    public GithubRepoInfoResponse getRepoInfos(GithubRepoUrl repoUrl, String token) {
        return restClient.get()
                .uri("/repos/{owner}/{repo}", repoUrl.getOwner(), repoUrl.getRepoName())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                .retrieve()
                .body(GithubRepoInfoResponse.class);
    }

    //풀리퀘스트 정보 얻기
    public GithubPullRequestReviewResponse getPullRequestInfo(GithubRepoUrl repoUrl, long pullRequestNumber, String token) {
        List<GithubReviewResponse> reviewResponses = restClient.get()
                .uri("/repos/{owner}/{repo}/pulls/{pull_number}/reviews", repoUrl.getOwner(), repoUrl.getRepoName(),
                        pullRequestNumber)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return GithubPullRequestReviewResponse.from(reviewResponses);
    }

    //웹훅 심기
    public void registerWebhook(GithubRepoUrl repoUrl, String webhookUrl, String token) {
        restClient.post()
                .uri("/repos/{owner}/{repo}/hooks", repoUrl.getOwner(), repoUrl.getRepoName())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                .body(GithubWebhookRequest.ofPullRequestEvent(webhookUrl));
    }
}
