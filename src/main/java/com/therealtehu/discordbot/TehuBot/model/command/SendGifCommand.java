package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.List;
public class SendGifCommand extends CommandWithFunctionality {
    private static final OptionData PROMPT_OPTION = new OptionData(
            OptionType.STRING,
            "gifprompt",
            "The keyword you would like to use.",
            true).setMinLength(1).setMaxLength(100);
    private static final CommandDataImpl COMMAND_DATA =
            (CommandDataImpl) Commands.slash("sendgif", "Send a gif from Tenor");

    private final TenorGifService tenorGifService;
    public SendGifCommand(TenorGifService tenorGifService) {
        super(COMMAND_DATA, List.of(PROMPT_OPTION));
        this.tenorGifService = tenorGifService;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        String prompt = event.getOption("gifprompt").getAsString();

        event.deferReply().queue();

        MessageEmbed messageEmbed = tenorGifService.getGifAsEmbed(prompt);
        MessageCreateData messageCreateData = new MessageCreateBuilder().addEmbeds(messageEmbed).build();
        event.getHook().sendMessage(messageCreateData).queue();
    }
}
