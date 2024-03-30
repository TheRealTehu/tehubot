package com.therealtehu.discordbot.TehuBot.listeners.command;

import com.therealtehu.discordbot.TehuBot.model.command.CommandWithFunctionality;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandManager extends ListenerAdapter {
    private final List<CommandWithFunctionality> commands;

    @Autowired
    public CommandManager(List<CommandWithFunctionality> commands) {
        this.commands = commands;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Optional<CommandWithFunctionality> chosenCommand = commands.stream()
                .filter(command -> command.getName().equals(event.getName()))
                .findFirst();
        if(chosenCommand.isPresent()) {
            chosenCommand.get().executeCommand(event);
        } else {
            event.getChannel().asTextChannel().sendMessage("Command: " + event.getName() + " not found!").queue();
        }
    }
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        addCommands(event);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        addCommands(event);
    }

    private void addCommands(GenericGuildEvent event) {
        event.getGuild().updateCommands().addCommands(commands).queue();
    }
}
