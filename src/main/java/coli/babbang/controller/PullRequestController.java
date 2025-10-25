package coli.babbang.controller;

import coli.babbang.dto.request.GithubWebhookEventRequest;
import coli.babbang.service.GithubPullRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PullRequestController {

    private final GithubPullRequestService githubPullRequestService;

    @PostMapping("/api/pull-requests/webhook")
    public ResponseEntity<Void> webhook(@RequestBody GithubWebhookEventRequest request) {
        githubPullRequestService.handleWebhookEvent(request);
        return ResponseEntity.ok().build();
    }
}
