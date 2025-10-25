package coli.babbang.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReminderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "approve_count")
    private long approveCount;

    @Column(unique = true, name = "github_repo_id")
    private long githubRepoId;

    @Column(name = "review_hour")
    private long reviewHour;

    public ReminderInfo(long approveCount, long githubRepoId, long reviewHour) {
        this(null, approveCount, githubRepoId, reviewHour);
    }
}
