package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildStatisticsData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildStatisticsRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuildStatisticsCommandTest {
    private GuildStatisticsCommand guildStatisticsCommand;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private GuildStatisticsRepository guildStatisticsRepositoryMock;
    @Mock
    private SlashCommandInteractionEvent eventMock;
    @Mock
    private Member memberMock;
    @Mock
    private Guild guildMock;
    @Mock
    private GuildData guildDataMock;

    @BeforeEach
    void setup() {
        guildStatisticsCommand = new GuildStatisticsCommand(messageSenderMock, guildStatisticsRepositoryMock);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsInDbReturnsGuildStatistics() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        GuildStatisticsData guildStatisticsData =
                new GuildStatisticsData(1L, guildDataMock, 0,0,0,0,0);
        when(guildStatisticsRepositoryMock.findByGuildId(1L)).thenReturn(Optional.of(guildStatisticsData));

        guildStatisticsCommand.executeCommand(eventMock);

        verify(guildStatisticsRepositoryMock).findByGuildId(1L);
        verify(messageSenderMock).reply(eventMock, guildStatisticsData.toString());
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndGuildIsNotInDbReturnsErrorMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildStatisticsRepositoryMock.findByGuildId(1L)).thenReturn(Optional.empty());

        guildStatisticsCommand.executeCommand(eventMock);

        verify(guildStatisticsRepositoryMock).findByGuildId(1L);
        verify(messageSenderMock).reply(eventMock, "DATABASE ERROR: Guild not found!");
    }

    @Test
    void executeCommandWhenMemberDoesNotHavePermissionDoesNothing() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(false);

        guildStatisticsCommand.executeCommand(eventMock);

        verifyNoInteractions(guildStatisticsRepositoryMock);
        verifyNoInteractions(messageSenderMock);
    }
}