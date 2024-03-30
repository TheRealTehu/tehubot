package com.therealtehu.discordbot.TehuBot.listeners;

import com.therealtehu.discordbot.TehuBot.model.command.CoinFlipCommand;
import com.therealtehu.discordbot.TehuBot.model.command.CommandWithFunctionality;
import com.therealtehu.discordbot.TehuBot.model.command.DiceRollCommand;
import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.guild.ServerJoinEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ListenerUtils {
    @Bean
    public List<CommandWithFunctionality> getCommands() {
        return List.of(
                new CoinFlipCommand(),
                new DiceRollCommand()
        );
    }
    @Bean
    public List<EventHandler> getEventHandlers() {
        return List.of(
                new ServerJoinEvent()
        );
    }
}
