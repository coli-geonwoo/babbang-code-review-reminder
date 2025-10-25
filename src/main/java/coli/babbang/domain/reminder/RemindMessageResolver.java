package coli.babbang.domain.reminder;

import coli.babbang.domain.github.Reviewer;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RemindMessageResolver {

    public String resolve(List<Reviewer> notReviewedReviewers){
        //TODO 메시지 작성하기
        String names = notReviewedReviewers.stream().map(Reviewer::getName).collect(Collectors.joining(","));
        return names + "리뷰해라";
    }
}
