package coli.babbang.domain;


import coli.babbang.exception.custom.BabbangException;
import coli.babbang.exception.errorcode.ErrorCode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordNotifier {

    private final DiscordProperties properties;
    private final JDA jda;

    public DiscordNotifier(DiscordProperties discordProperties) {
        this.properties = discordProperties;
        this.jda = initializeJda(properties.getToken());
    }

    private JDA initializeJda(String token) {
        try {
            return JDABuilder.createDefault(token).build().awaitReady();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BabbangException(ErrorCode.DISCORD_PROPERTIES_EMPTY);
        }
    }

    public void sendMessage(String message, long channelId) {
        TextChannel channel = jda.getTextChannelById(channelId);
        channel.sendMessage(message).queue();
    }
}
