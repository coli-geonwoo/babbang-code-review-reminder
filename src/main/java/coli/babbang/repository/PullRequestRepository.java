package coli.babbang.repository;

import coli.babbang.domain.github.GithubPullRequest;
import coli.babbang.domain.github.ReviewStatus;
import coli.babbang.exception.custom.BabbangException;
import coli.babbang.exception.errorcode.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<GithubPullRequest, Long> {

    Optional<GithubPullRequest> findByExternalId(long externalId);

    List<GithubPullRequest> findAllByStatus(ReviewStatus reviewStatus);

    default GithubPullRequest getByExternalId(long externalId) {
        return findByExternalId(externalId)
                .orElseThrow(() -> new BabbangException(ErrorCode.PULL_REQUEST_NOT_FOUND));
    }

    default GithubPullRequest getByAppId(long id) {
        return findById(id)
                .orElseThrow(() -> new BabbangException(ErrorCode.PULL_REQUEST_NOT_FOUND));
    }

}
