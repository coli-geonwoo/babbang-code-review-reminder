package coli.babbang.repository;

import coli.babbang.domain.github.Reviewer;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {

    List<Reviewer> findAllByRepoId(long repoId);

    default List<Reviewer> findStupidReviewers(Set<String> alreadyReviewedReviewer, long repoId) {
        return findAllByRepoId(repoId).stream()
                .filter(reviewer -> !alreadyReviewedReviewer.contains(reviewer.getName()))
                .toList();
    }
}
