package coli.babbang.repository;

import coli.babbang.domain.GithubPullRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<GithubPullRequest, Long> {

}
