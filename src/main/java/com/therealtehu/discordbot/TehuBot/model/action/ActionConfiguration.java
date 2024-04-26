package com.therealtehu.discordbot.TehuBot.model.action;

import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActionConfiguration {
    @Bean
    public MessageCreateBuilder getMessageCreateBuilder() {
        return new MessageCreateBuilder();
    }
}
