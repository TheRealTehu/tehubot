package com.therealtehu.discordbot.TehuBot.model.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.List;

public abstract class CommandWithFunctionality extends CommandDataImpl {
    public CommandWithFunctionality(CommandDataImpl commandData) {
        super(commandData.getName(), commandData.getDescription());
    }

    public CommandWithFunctionality(CommandDataImpl commandData, List<OptionData> options) {
        super(commandData.getName(), commandData.getDescription());
        super.addOptions(options);
    }

    public abstract void executeCommand(SlashCommandInteractionEvent event);
}
