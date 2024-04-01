package com.therealtehu.discordbot.TehuBot.listeners;

import com.therealtehu.discordbot.TehuBot.model.command.*;
import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.guild.ServerJoinEvent;
import com.therealtehu.discordbot.TehuBot.model.event.guild.ServerNewMemberEvent;
import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.WikiArticleService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ListenerUtils {
    private final TenorGifService tenorGifService;
    private final WikiArticleService wikiArticleService;
    private final MessageSender messageSender;

    @Autowired
    public ListenerUtils(TenorGifService tenorGifService, WikiArticleService wikiArticleService, MessageSender messageSender) {
        this.tenorGifService = tenorGifService;
        this.wikiArticleService = wikiArticleService;
        this.messageSender = messageSender;
    }

    @Bean
    public List<CommandWithFunctionality> getCommands() {
        return List.of(
                new CoinFlipCommand(messageSender),
                new DiceRollCommand(messageSender),
                new SendGifCommand(tenorGifService, messageSender),
                new GetWikiCommand(wikiArticleService, messageSender)
        );
    }
    @Bean
    public List<EventHandler> getEventHandlers() {
        return List.of(
                new ServerJoinEvent(messageSender),
                new ServerNewMemberEvent(tenorGifService, messageSender)
        );
    }
}
