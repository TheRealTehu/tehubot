package com.therealtehu.discordbot.TehuBot.listeners;

import com.therealtehu.discordbot.TehuBot.model.command.*;
import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.guild.ServerJoinEvent;
import com.therealtehu.discordbot.TehuBot.model.event.guild.ServerNewMemberEvent;
import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.WikiArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ListenerUtils {
    private final TenorGifService tenorGifService;
    private final WikiArticleService wikiArticleService;

    @Autowired
    public ListenerUtils(TenorGifService tenorGifService, WikiArticleService wikiArticleService) {
        this.tenorGifService = tenorGifService;
        this.wikiArticleService = wikiArticleService;
    }

    @Bean
    public List<CommandWithFunctionality> getCommands() {
        return List.of(
                new CoinFlipCommand(),
                new DiceRollCommand(),
                new SendGifCommand(tenorGifService),
                new GetWikiCommand(wikiArticleService)
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
