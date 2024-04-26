package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.List;

public abstract class CommandWithFunctionality extends CommandDataImpl {

    protected final MessageSender messageSender;
    public CommandWithFunctionality(String commandName, String commandDescription, MessageSender messageSender) {
        super(commandName, commandDescription);
        this.messageSender = messageSender;
    }

    public CommandWithFunctionality(String commandName, String commandDescription, List<OptionData> options, MessageSender messageSender) {
        super(commandName, commandDescription);
        super.addOptions(options);
        this.messageSender = messageSender;
    }

    public abstract void executeCommand(SlashCommandInteractionEvent event);
}
