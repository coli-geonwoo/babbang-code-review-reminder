package coli.babbang.client;

import coli.babbang.domain.GithubRepoUrl;
import coli.babbang.dto.request.GithubWebhookRequest;
import coli.babbang.dto.response.GithubRepoInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubClient {

    private static final String BEARER_PREFIX = "Bearer ";

    private final RestClient restClient;

    public GithubClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public GithubRepoInfoResponse getRepoInfos(GithubRepoUrl repoUrl, String token) {
        return restClient.get()
                .uri("https://api.github.com/repos/{owner}/{repo}", repoUrl.getOwner(), repoUrl.getRepoName())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                .retrieve()
                .body(GithubRepoInfoResponse.class);
    }

    //풀리퀘스트 정보 얻기
    public void getPullRequestInfo(long githubPullRequestId, String token) {

    }

    //웹훅 심기
    public void registerWebhook(GithubRepoUrl repoUrl, String webhookUrl, String token) {
        restClient.post()
                .uri("https://api.github.com/repos/{owner}/{repo}/hooks", repoUrl.getOwner(), repoUrl.getRepoName())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                .body(GithubWebhookRequest.ofPullRequestEvent(webhookUrl));
    }
}
