package coli.babbang.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubPullRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id")
    private long externalId;

    @Column(name = "repo_id")
    private long repoId;

    @NotBlank
    private String openUser;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    public GithubPullRequest(long externalId, long repoId, String openUser, ReviewStatus status) {
        this(null, externalId, repoId, openUser, status);
    }

    public void merge() {
        this.status = ReviewStatus.DONE;
    }

    public void reviewing() {
        this.status = ReviewStatus.WAITING;
    }

    public boolean isMerged() {
        return this.status == ReviewStatus.DONE;
    }
}
