package com.therealtehu.discordbot.TehuBot.listeners;

import com.therealtehu.discordbot.TehuBot.listeners.event.EventListener;
import com.therealtehu.discordbot.TehuBot.model.action.command.*;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.PollCommand;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.guild.ServerNewMemberEvent;
import com.therealtehu.discordbot.TehuBot.model.action.event.guild.server_join.ChannelChoosingDropDownEvent;
import com.therealtehu.discordbot.TehuBot.model.action.event.guild.server_join.ServerJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ListenerConfiguration {
    @Bean
    @Autowired
    public List<CommandWithFunctionality> getCommands(CoinFlipCommand coinFlipCommand, DiceRollCommand diceRollCommand,
                                                      SendGifCommand sendGifCommand, GetWikiCommand getWikiCommand,
                                                      PollCommand pollCommand, GuildStatisticsCommand guildStatisticsCommand,
                                                      SetupCommand setupCommand) {
        return List.of(
                coinFlipCommand,
                diceRollCommand,
                sendGifCommand,
                getWikiCommand,
                pollCommand,
                getWikiCommand,
                guildStatisticsCommand,
                setupCommand
        );
    }
    @Bean
    @Autowired
    public List<EventHandler> getEventHandlers(ServerJoinEvent serverJoinEvent, ServerNewMemberEvent serverNewMemberEvent,
                                               ChannelChoosingDropDownEvent channelChoosingDropDownEvent) {
        return List.of(
                serverJoinEvent,
                serverNewMemberEvent,
                channelChoosingDropDownEvent
        );
    }

    @Bean
    public Logger getEventListenerLogger() {
        return LoggerFactory.getLogger(EventListener.class);
    }
}
