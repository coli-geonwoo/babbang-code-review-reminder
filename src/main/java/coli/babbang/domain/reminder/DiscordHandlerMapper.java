package coli.babbang.domain.reminder;


import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscordHandlerMapper {
    //BE
    콜리("coli-geonwoo", "<@1258592645220139041>"),
    커찬("leegwichan", "<@273420257975074826>"),
    비토("unifolio0", "<@1254102447937294437>"),

    //FE
    치코("jaeml06", "<@553901817088573440>"),
    썬데이("useon", "<@1259427716579725382>"),
    숀("i-meant-to-be", "<@282190056477687809>"),
    에버("helenason", "<@1249700746467999897>"),
    기본("default", "@바빵이")
    ;

    private final String githubHandler;
    private final String discordHandler;

    public static String mapToDiscordHandler(String githubHandler) {
        return Stream.of(values())
                .filter(member -> member.githubHandler.equals(githubHandler))
                .findAny()
                .orElse(기본)
                .getDiscordHandler();
    }
}
