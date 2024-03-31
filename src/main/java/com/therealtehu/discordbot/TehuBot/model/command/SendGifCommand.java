package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class SendGifCommand extends CommandWithFunctionality {
    private static final OptionData promptOption = new OptionData(
            OptionType.STRING,
            "gifprompt",
            "The keyword you would like to use.",
            true).setMinLength(1).setMaxLength(100);
    private static final CommandDataImpl commandData =
            (CommandDataImpl) Commands.slash("sendgif", "Send a gif from Tenor");

    private final TenorGifService tenorGifService;

    @Autowired
    public SendGifCommand(TenorGifService tenorGifService) {
        super(commandData, List.of(promptOption));
        this.tenorGifService = tenorGifService;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        String prompt = event.getOption("gifprompt").getAsString();

        event.deferReply().queue();

        MessageEmbed messageEmbed = new EmbedBuilder()
                .setTitle(prompt + " gif:")
                .setImage(tenorGifService.searchGifUrlOnTenor(prompt))
                .build();
        MessageCreateData messageCreateData = new MessageCreateBuilder().addEmbeds(messageEmbed).build();
        event.getHook().sendMessage(messageCreateData).queue();
    }
}
