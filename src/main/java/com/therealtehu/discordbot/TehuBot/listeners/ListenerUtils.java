package com.therealtehu.discordbot.TehuBot.listeners;

import com.therealtehu.discordbot.TehuBot.model.command.CoinFlipCommand;
import com.therealtehu.discordbot.TehuBot.model.command.CommandWithFunctionality;
import com.therealtehu.discordbot.TehuBot.model.command.DiceRollCommand;
import com.therealtehu.discordbot.TehuBot.model.command.SendGifCommand;
import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.guild.ServerJoinEvent;
import com.therealtehu.discordbot.TehuBot.model.event.guild.ServerNewMemberEvent;
import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ListenerUtils {
    private final TenorGifService tenorGifService;

    @Autowired
    public ListenerUtils(TenorGifService tenorGifService) {
        this.tenorGifService = tenorGifService;
    }

    @Bean
    public List<CommandWithFunctionality> getCommands() {
        return List.of(
                new CoinFlipCommand(),
                new DiceRollCommand(),
                new SendGifCommand(tenorGifService)
        );
    }
    @Bean
    public List<EventHandler> getEventHandlers() {
        return List.of(
                new ServerJoinEvent(),
                new ServerNewMemberEvent(tenorGifService)
        );
    }
}
