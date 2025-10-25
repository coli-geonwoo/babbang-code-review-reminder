package coli.babbang.domain.notifier;

import coli.babbang.exception.custom.BabbangException;
import coli.babbang.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "discord")
public class DiscordProperties {

    private final String token;

    public DiscordProperties(String token) {
        validate(token);
        this.token = token;
    }

    private void validate(String element) {
        if (element == null || element.isBlank()) {
            throw new BabbangException(ErrorCode.METHOD_NOT_SUPPORTED);
        }
    }
}
