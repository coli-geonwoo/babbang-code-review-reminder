package coli.babbang.repository;

import coli.babbang.domain.ReminderInfo;
import coli.babbang.exception.custom.BabbangException;
import coli.babbang.exception.errorcode.ErrorCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<ReminderInfo, Long> {

    Optional<ReminderInfo> findByGithubRepoId(long githubRepoId);

    default ReminderInfo getByRepositoryId(Long repoId) {
        return findByGithubRepoId(repoId)
                .orElseThrow(() -> new BabbangException(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND));
    }

}
