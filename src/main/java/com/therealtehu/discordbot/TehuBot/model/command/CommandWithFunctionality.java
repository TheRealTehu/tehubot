package com.therealtehu.discordbot.TehuBot.model.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public abstract class CommandWithFunctionality extends CommandDataImpl {
    public CommandWithFunctionality(CommandDataImpl commandData) {
        super(commandData.getName(), commandData.getDescription());
    }

    public abstract void executeCommand(SlashCommandInteractionEvent event);
}
