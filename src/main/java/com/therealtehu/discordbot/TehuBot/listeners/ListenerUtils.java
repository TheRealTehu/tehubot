package com.therealtehu.discordbot.TehuBot.listeners;

import com.therealtehu.discordbot.TehuBot.model.button.ButtonWithFunctionality;
import com.therealtehu.discordbot.TehuBot.model.button.setup.*;
import com.therealtehu.discordbot.TehuBot.model.command.*;
import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.guild.ServerJoinEvent;
import com.therealtehu.discordbot.TehuBot.model.event.guild.ServerNewMemberEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ListenerUtils {
    @Bean
    @Autowired
    public List<CommandWithFunctionality> getCommands(CoinFlipCommand coinFlipCommand, DiceRollCommand diceRollCommand,
                                                      SendGifCommand sendGifCommand, GetWikiCommand getWikiCommand) {
        return List.of(
                coinFlipCommand,
                diceRollCommand,
                sendGifCommand,
                getWikiCommand
        );
    }
    @Bean
    @Autowired
    public List<EventHandler> getEventHandlers(ServerJoinEvent serverJoinEvent, ServerNewMemberEvent serverNewMemberEvent) {
        return List.of(
                serverJoinEvent,
                serverNewMemberEvent
        );
    }

    @Bean
    @Autowired
    public List<ButtonWithFunctionality> getServerJoinEventButtons(AlwaysInCommandChannelButton alwaysInCommandChannelButton,
                                                                   CreateOneChannelForAllButton createOneChannelForAllButton,
                                                                   CreateChannelsForCategoriesButton createChannelsForCategoriesButton,
                                                                   SpecifyOneChannelForAllButton specifyOneChannelForAllButton,
                                                                   SpecifyChannelsForCategoriesButton specifyChannelsForCategoriesButton) {
        return List.of(
                alwaysInCommandChannelButton,
                createOneChannelForAllButton,
                createChannelsForCategoriesButton,
                specifyOneChannelForAllButton,
                specifyChannelsForCategoriesButton
        );
    }
}
