package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.WikiArticleService;
import com.therealtehu.discordbot.TehuBot.service.WikiTextConverter;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.List;

public class GetWikiCommand extends CommandWithFunctionality {
    private static final OptionData TITLE_OPTION = new OptionData(
            OptionType.STRING,
            "wikititle",
            "The title of the wiki article you would like to find.",
            true).setMinLength(1).setMaxLength(100);

    private static final String COMMAND_NAME = "getwiki";

    private static final String COMMAND_DESCRIPTION = "Get a wiki article.";
    private final WikiArticleService wikiArticleService;

    private static final int MAX_MESSAGE_LENGTH = 4095;

    public GetWikiCommand(WikiArticleService wikiArticleService, MessageSender messageSender) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, List.of(TITLE_OPTION), messageSender);
        this.wikiArticleService = wikiArticleService;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        String title = event.getOption("wikititle").getAsString();
        String articleWikiText = wikiArticleService.getWikiArticle(title);
        String plainText = WikiTextConverter.convertToPlainText(articleWikiText);
        if(plainText.length() > MAX_MESSAGE_LENGTH) {
            plainText = plainText.substring(0, MAX_MESSAGE_LENGTH - 2) + "...";
        } else if(plainText.startsWith("<!--")) {
            plainText = "Wiki API too busy, please try again later!";
        }
        MessageEmbed messageEmbed = new EmbedBuilder()
                .setTitle("Wiki article: " + title)
                .setColor(Color.BLUE)
                .setDescription(plainText)
                .build();

        messageSender.sendMessageEmbedOnHook(event.getHook(), messageEmbed);
    }
}
