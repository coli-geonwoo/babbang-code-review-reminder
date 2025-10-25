package coli.babbang.repository;

import coli.babbang.domain.notifier.DiscordProperty;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscordPropertyRepository extends JpaRepository<DiscordProperty, Long> {

    Optional<DiscordProperty> findByRepoId(long repoId);

}
