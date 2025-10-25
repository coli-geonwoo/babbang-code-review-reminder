package coli.babbang.repository;

import coli.babbang.domain.GithubPullRequest;
import coli.babbang.exception.custom.BabbangException;
import coli.babbang.exception.errorcode.ErrorCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<GithubPullRequest, Long> {

    Optional<GithubPullRequest> findByExternalId(long externalId);

    default GithubPullRequest getByExternalId(long externalId) {
        return findByExternalId(externalId)
                .orElseThrow(() -> new BabbangException(ErrorCode.PULL_REQUEST_NOT_FOUND));
    }
}
