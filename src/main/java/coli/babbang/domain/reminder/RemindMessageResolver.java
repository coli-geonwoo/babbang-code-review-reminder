package coli.babbang.domain.reminder;

import coli.babbang.domain.github.GithubPullRequest;
import coli.babbang.domain.github.GithubRepo;
import coli.babbang.domain.github.Reviewer;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RemindMessageResolver {

    public String resolve(
            List<Reviewer> notReviewedReviewers,
            ReminderType reminderType,
            GithubRepo repo,
            GithubPullRequest githubPullRequest
    ) {
        String header = reminderType.getHeader();
        String body = getBody(notReviewedReviewers);
        String url = resolvePullRequestUrl(repo, githubPullRequest);

        StringJoiner joiner = new StringJoiner(System.lineSeparator().repeat(2));
        joiner.add(body);
        joiner.add(header);
        joiner.add(url);
        return joiner.toString();
    }

    private String getBody(List<Reviewer> notReviewedReviewers) {
        return notReviewedReviewers
                .stream()
                .map(reviewer -> DiscordHandlerMapper.mapToDiscordHandler(reviewer.getName()))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String resolvePullRequestUrl(
            GithubRepo repo,
            GithubPullRequest githubPullRequest
    ) {
        return repo.getUrl() + "/pull/" + githubPullRequest.getNumber();
    }
}
