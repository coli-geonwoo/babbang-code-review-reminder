package coli.babbang.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.MediaType;

import java.util.List;

public record GithubWebhookRequest(
        String name,
        boolean active,
        List<String> events,
        GithubWebhookConfig config
) {
    public record GithubWebhookConfig(
            String url,
            @JsonProperty("content_type")
            String contentType
    ) {
    }

    public static GithubWebhookRequest ofPullRequestEvent(String webhookUrl) {
        return new GithubWebhookRequest(
                "web",
                true,
                List.of("pull_request"),
                new GithubWebhookConfig(
                        webhookUrl,
                        MediaType.APPLICATION_JSON.getSubtype()
                )
        );
    }
}

