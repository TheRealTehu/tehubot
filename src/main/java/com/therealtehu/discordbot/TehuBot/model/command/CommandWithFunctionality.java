package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.List;

public abstract class CommandWithFunctionality extends CommandDataImpl {

    protected final MessageSender messageSender;
    public CommandWithFunctionality(CommandDataImpl commandData, MessageSender messageSender) {
        super(commandData.getName(), commandData.getDescription());
        this.messageSender = messageSender;
    }

    public CommandWithFunctionality(CommandDataImpl commandData, List<OptionData> options, MessageSender messageSender) {
        super(commandData.getName(), commandData.getDescription());
        super.addOptions(options);
        this.messageSender = messageSender;
    }

    public abstract void executeCommand(SlashCommandInteractionEvent event);
}
