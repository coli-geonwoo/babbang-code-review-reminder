package coli.babbang.config;

import coli.babbang.domain.DiscordNotifier;
import coli.babbang.domain.DiscordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DiscordProperties.class)
public class NotiConfig {

    private final DiscordProperties discordProperties;

    @Bean
    public DiscordNotifier discordNotifier() {
        return new DiscordNotifier(discordProperties);
    }
}
