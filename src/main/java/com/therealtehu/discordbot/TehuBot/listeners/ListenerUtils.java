package com.therealtehu.discordbot.TehuBot.listeners;

import com.therealtehu.discordbot.TehuBot.model.command.CoinFlipCommand;
import com.therealtehu.discordbot.TehuBot.model.command.CommandWithFunctionality;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ListenerUtils {
    @Bean
    public List<CommandWithFunctionality> getCommands() {
        return List.of(
                new CoinFlipCommand()
        );
    }
}
