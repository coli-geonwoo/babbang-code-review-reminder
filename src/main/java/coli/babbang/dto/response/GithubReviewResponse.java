package coli.babbang.dto.response;

public record GithubReviewResponse(
        User user,
        String state

) {
    public record User(
            String login
    ) {
    }

    public boolean isApproved() {
        return state.equals("APPROVED");
    }

    public String getUserName() {
        return user.login;
    }
}
