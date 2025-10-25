package coli.babbang.domain.notifier;


import coli.babbang.exception.custom.BabbangException;
import coli.babbang.exception.errorcode.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

@Component
public class DiscordNotifier {

    private final Map<String, JDA> jdaMap = new HashMap<>();

    public void sendMessage(String message, DiscordProperty discordProperty) {
        JDA jda = jdaMap.getOrDefault(message.toLowerCase(), initializeJda(discordProperty.getDiscordBotToken()));
        TextChannel channel = jda.getTextChannelById(discordProperty.getChannelId());
        channel.sendMessage(message).queue();
    }

    private JDA initializeJda(String token) {
        try {
            return JDABuilder.createDefault(token).build().awaitReady();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BabbangException(ErrorCode.DISCORD_PROPERTIES_EMPTY);
        }
    }
}
