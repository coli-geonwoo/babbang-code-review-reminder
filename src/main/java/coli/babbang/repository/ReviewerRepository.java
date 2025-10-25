package coli.babbang.repository;

import coli.babbang.domain.Reviewer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {

    List<Reviewer> findAllByRepoId(long repoId);
}
