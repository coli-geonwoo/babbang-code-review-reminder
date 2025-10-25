package coli.babbang.repository;

import coli.babbang.domain.ReminderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<ReminderInfo, Long> {


}
