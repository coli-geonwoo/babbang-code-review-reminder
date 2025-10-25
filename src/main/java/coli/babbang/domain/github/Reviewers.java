package coli.babbang.domain.github;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Reviewers {

    private final List<Reviewer> values;

    public Reviewers(List<String> names, long repoId) {
        this.values = names.stream()
                .map(name -> new Reviewer(repoId, name))
                .toList();
    }
}
