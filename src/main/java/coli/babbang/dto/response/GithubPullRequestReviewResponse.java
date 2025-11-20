package coli.babbang.dto.response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record GithubPullRequestReviewResponse(
        long approveCount,
        long reviewCount,
        List<String> reviewTeamMateNames,
        List<String> approveTeamMateNames
) {

    public static GithubPullRequestReviewResponse from(List<GithubReviewResponse> responses){
        long approveCount = 0;
        Set<String> reviewTeamMateNames = new HashSet<>();
        Set<String> approveTeamMateNames = new HashSet<>();
        for (GithubReviewResponse response : responses) {
            reviewTeamMateNames.add(response.getUserName());
            if(response.isApproved() && !approveTeamMateNames.contains(response.getUserName())) {
                approveCount++;
                approveTeamMateNames.add(response.getUserName());
            }
        }
        return new GithubPullRequestReviewResponse(
                approveCount,
                reviewTeamMateNames.size(),
                new ArrayList<>(reviewTeamMateNames),
                new ArrayList<>(approveTeamMateNames)
        );
    }
}
