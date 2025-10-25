package coli.babbang.repository;

import coli.babbang.domain.GithubPullRequest;
import coli.babbang.domain.GithubRepo;
import coli.babbang.exception.custom.BabbangException;
import coli.babbang.exception.errorcode.ErrorCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepoRepository extends JpaRepository<GithubRepo, Long> {

    Optional<GithubRepo> findByExternalId(long externalId);

    default GithubRepo getByExternalId(long externalId) {
        return findByExternalId(externalId)
                .orElseThrow(() -> new BabbangException(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND));
    }
}
