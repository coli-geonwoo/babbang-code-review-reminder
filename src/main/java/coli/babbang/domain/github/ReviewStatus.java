package coli.babbang.domain.github;

public enum ReviewStatus {

    PENDING, //리뷰 기간 미 경과
    WAITING, //리뷰 기간 경과 후, 대기 중
    DONE,
    ;
}
