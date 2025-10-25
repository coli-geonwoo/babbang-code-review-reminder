package coli.babbang.dto.request;

import java.util.List;

public record ReminderCreateRequest(
        String githubRepoUrl,
        long approveCount,
        long reviewDay,
        long reviewHour,
        List<String> reviewers
) {

}
