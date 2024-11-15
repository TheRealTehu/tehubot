package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GetWikiData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GetWikiRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.WikiArticleService;
import com.therealtehu.discordbot.TehuBot.service.WikiTextConverter;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class GetWikiCommand extends CommandWithFunctionality {
    private static final int MIN_TITLE_LENGTH = 1;
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_MESSAGE_LENGTH = 4095;
    private static final OptionData TITLE_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.GET_WIKI_TITLE_OPTION.getOptionName(),
            "The title of the wiki article you would like to find.",
            true).setMinLength(MIN_TITLE_LENGTH).setMaxLength(MAX_TITLE_LENGTH);

    private static final String COMMAND_NAME = "getwiki";
    private static final String COMMAND_DESCRIPTION = "Get a wiki article.";
    private final WikiArticleService wikiArticleService;
    private final GetWikiRepository getWikiRepository;
    private final GuildRepository guildRepository;

    @Autowired
    public GetWikiCommand(WikiArticleService wikiArticleService, MessageSender messageSender,
                          GetWikiRepository getWikiRepository, GuildRepository guildRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, List.of(TITLE_OPTION), messageSender);
        this.wikiArticleService = wikiArticleService;
        this.getWikiRepository = getWikiRepository;
        this.guildRepository = guildRepository;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        String title = event.getOption(OptionName.GET_WIKI_TITLE_OPTION.getOptionName()).getAsString();
        String articleWikiText = wikiArticleService.getWikiArticle(title);

        if (articleWikiText.startsWith("ERROR")) {
            messageSender.reply(event, articleWikiText);
        } else if (articleWikiText.startsWith("<!--")) {
            messageSender.reply(event, "Wiki API too busy, please try again later!");
        } else {
            try {
                String discordText = convertToDiscordText(articleWikiText);
                saveToDatabase(event, title);
                replyToEvent(event, title, discordText);
            } catch (NoSuchElementException e) {
                messageSender.reply(event, e.getMessage());
            }
        }
    }

    private String convertToDiscordText(String articleWikiText) {
        String discordText = WikiTextConverter.convertToPlainText(articleWikiText);
        if (discordText.length() > MAX_MESSAGE_LENGTH) {
            discordText = discordText.substring(0, MAX_MESSAGE_LENGTH - 2) + "...";
        }
        return discordText;
    }

    private void replyToEvent(SlashCommandInteractionEvent event, String title, String plainText) {
        MessageEmbed messageEmbed = new EmbedBuilder()
                .setTitle("Wiki article: " + title)
                .setColor(Color.BLUE)
                .setDescription(plainText)
                .build();

        messageSender.sendMessageEmbedOnHook(event.getHook(), messageEmbed);
    }

    private void saveToDatabase(SlashCommandInteractionEvent event, String searchedTopic) {
        GetWikiData getWikiData = new GetWikiData();
        getWikiData.setSearchedTopic(searchedTopic);
        Optional<GuildData> guildData = guildRepository.findById(event.getGuild().getIdLong());
        if (guildData.isEmpty()) {
            throw new NoSuchElementException("DATABASE ERROR: Guild not found!");
        }
        getWikiData.setGuild(guildData.get());
        getWikiRepository.save(getWikiData);
    }
}
