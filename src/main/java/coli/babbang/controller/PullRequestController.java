package coli.babbang.controller;

import coli.babbang.dto.request.GithubWebhookEventRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PullRequestController {

    @PostMapping("/api/pull-requests/webhook")
    public void webhook(@RequestBody GithubWebhookEventRequest request) {

    }
}
