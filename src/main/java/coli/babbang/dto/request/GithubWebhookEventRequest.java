package coli.babbang.dto.request;

/**
 * docs : https://docs.github.com/ko/webhooks/webhook-events-and-payloads#pull_request
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record GithubWebhookEventRequest(
        String action,
        int number,
        @JsonProperty("pull_request") PullRequest pullRequest,
        Repository repository
) {
    public record PullRequest(
            String url,
            long id,
            @JsonProperty("diff_url") String diffUrl,
            // open, closed
            String state,
            User user,
            Boolean merged,
            @JsonProperty("merged_at") LocalDateTime mergedAt
    ) {
    }

    public record User(
            String login,
            long id
    ) {
    }

    public record Repository(
            long id,
            String name,
            @JsonProperty("private") boolean isPrivate,
            User owner
    ) {
    }

    public long getRepositoryId() {
        return repository.id;
    }

    public String getOpenUser() {
        return pullRequest.user.login;
    }

    public long getExternalId() {
        return pullRequest.id;
    }

    public boolean isMerged() {
        if ("closed".equals(action)
                && pullRequest != null
                && pullRequest.merged != null
                && pullRequest.mergedAt != null
        ) {
            return pullRequest.merged;
        } else {
            return false;
        }
    }

    public boolean isOpened() {
        return "opened".equals(action)
                && pullRequest != null;
    }
}

