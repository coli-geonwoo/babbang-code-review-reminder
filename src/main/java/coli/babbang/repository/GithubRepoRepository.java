package coli.babbang.repository;

import coli.babbang.domain.GithubRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepoRepository extends JpaRepository<GithubRepo, Long> {

}
