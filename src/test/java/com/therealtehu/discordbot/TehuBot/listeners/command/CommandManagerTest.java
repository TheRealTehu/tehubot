package com.therealtehu.discordbot.TehuBot.listeners.command;

import com.therealtehu.discordbot.TehuBot.model.command.CommandWithFunctionality;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandManagerTest {
    private CommandManager commandManager;
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);

    private final CommandWithFunctionality mockCommand = Mockito.mock(CommandWithFunctionality.class);
    private final SlashCommandInteractionEvent mockSlashCommandEvent = Mockito.mock(SlashCommandInteractionEvent.class);

    @Test
    void onSlashCommandInteractionWhenCommandListIsEmptySendCommandNotFoundMessage() {
        commandManager = new CommandManager(List.of(), mockMessageSender);

        MessageChannelUnion mockUnionChannel = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);

        String errorMessage = "Command: commandname not found!";

        when(mockSlashCommandEvent.getName()).thenReturn("commandname");
        when(mockSlashCommandEvent.getChannel()).thenReturn(mockUnionChannel);
        when(mockUnionChannel.asTextChannel()).thenReturn(mockTextChannel);

        commandManager.onSlashCommandInteraction(mockSlashCommandEvent);

        verify(mockMessageSender).sendMessage(mockTextChannel, errorMessage);
    }

    @Test
    void onSlashCommandInteractionWhenOneCommandIsPresentButNotTheCalledCommandSendCommandNotFoundMessage() {
        commandManager = new CommandManager(List.of(mockCommand), mockMessageSender);

        MessageChannelUnion mockUnionChannel = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);

        String errorMessage = "Command: commandname not found!";

        when(mockCommand.getName()).thenReturn("othercommand");
        when(mockSlashCommandEvent.getName()).thenReturn("commandname");
        when(mockSlashCommandEvent.getChannel()).thenReturn(mockUnionChannel);
        when(mockUnionChannel.asTextChannel()).thenReturn(mockTextChannel);

        commandManager.onSlashCommandInteraction(mockSlashCommandEvent);

        verify(mockMessageSender).sendMessage(mockTextChannel, errorMessage);
    }

    @Test
    void onSlashCommandInteractionWhenOneCommandIsPresentAndIsTheCalledCommandExecutesCommand() {
        commandManager = new CommandManager(List.of(mockCommand), mockMessageSender);

        when(mockCommand.getName()).thenReturn("commandname");
        when(mockSlashCommandEvent.getName()).thenReturn("commandname");

        commandManager.onSlashCommandInteraction(mockSlashCommandEvent);

        verify(mockCommand).executeCommand(mockSlashCommandEvent);
    }

    @Test
    void onSlashCommandInteractionWhenCommandListContainsMoreCommandAndTheCalledCommandExecutesCommand() {
        CommandWithFunctionality otherMockCommand = Mockito.mock(CommandWithFunctionality.class);
        commandManager = new CommandManager(List.of(mockCommand, otherMockCommand), mockMessageSender);

        when(mockCommand.getName()).thenReturn("commandname");
        when(otherMockCommand.getName()).thenReturn("othercommand");
        when(mockSlashCommandEvent.getName()).thenReturn("commandname");

        commandManager.onSlashCommandInteraction(mockSlashCommandEvent);

        verify(mockCommand).executeCommand(mockSlashCommandEvent);
    }

    @Test
    void onGuildReady() {
        List<CommandWithFunctionality> commands = List.of(mockCommand);
        commandManager = new CommandManager(commands, mockMessageSender);

        GuildReadyEvent mockGuildReadyEvent = Mockito.mock(GuildReadyEvent.class);

        Guild mockGuild = Mockito.mock(Guild.class);
        CommandListUpdateAction mockCommandListUpdateAction = Mockito.mock(CommandListUpdateAction.class);

        when(mockGuildReadyEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.updateCommands()).thenReturn(mockCommandListUpdateAction);
        when(mockCommandListUpdateAction.addCommands(commands)).thenReturn(mockCommandListUpdateAction);

        commandManager.onGuildReady(mockGuildReadyEvent);

        verify(mockGuildReadyEvent.getGuild().updateCommands().addCommands(commands)).queue();
    }

    @Test
    void onGuildJoin() {
        List<CommandWithFunctionality> commands = List.of(mockCommand);
        commandManager = new CommandManager(commands, mockMessageSender);

        GuildJoinEvent mockGuildJoinEvent = Mockito.mock(GuildJoinEvent.class);

        Guild mockGuild = Mockito.mock(Guild.class);
        CommandListUpdateAction mockCommandListUpdateAction = Mockito.mock(CommandListUpdateAction.class);

        when(mockGuildJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.updateCommands()).thenReturn(mockCommandListUpdateAction);
        when(mockCommandListUpdateAction.addCommands(commands)).thenReturn(mockCommandListUpdateAction);

        commandManager.onGuildJoin(mockGuildJoinEvent);

        verify(mockGuildJoinEvent.getGuild().updateCommands().addCommands(commands)).queue();
    }
}