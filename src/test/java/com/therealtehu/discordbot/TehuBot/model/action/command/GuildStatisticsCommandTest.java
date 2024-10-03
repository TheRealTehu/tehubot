package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildStatisticsData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildStatisticsRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    private Guild guildMock;
    @Mock
    private GuildData guildDataMock;

    @BeforeEach
    void setup() {
        guildStatisticsCommand = new GuildStatisticsCommand(messageSenderMock, guildStatisticsRepositoryMock);
    }

    @Test
    void executeCommandWhenGuildIsInDbReturnsGuildStatistics() {
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        GuildStatisticsData guildStatisticsData = new GuildStatisticsData(1L, guildDataMock, 0,0);
        when(guildStatisticsRepositoryMock.findByGuildId(1L)).thenReturn(Optional.of(guildStatisticsData));

        guildStatisticsCommand.executeCommand(eventMock);

        verify(guildStatisticsRepositoryMock).findByGuildId(1L);
        verify(messageSenderMock).replyToEvent(eventMock, guildStatisticsData.toString());
    }

    @Test
    void executeCommandWhenGuildIsNotInDbReturnsErrorMessage() {
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildStatisticsRepositoryMock.findByGuildId(1L)).thenReturn(Optional.empty());

        guildStatisticsCommand.executeCommand(eventMock);

        verify(guildStatisticsRepositoryMock).findByGuildId(1L);
        verify(messageSenderMock).replyToEvent(eventMock, "DATABASE ERROR: Guild not found!");
    }
}