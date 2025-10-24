package coli.babbang.dto.response;

import java.util.List;

public record GithubPullRequestReviewResponse(
        long approveCount,
        List<String> approveTeamMateNames
) {

}
