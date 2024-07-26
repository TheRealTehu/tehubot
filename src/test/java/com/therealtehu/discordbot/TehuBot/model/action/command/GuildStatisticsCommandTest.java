package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildStatisticsData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildStatisticsRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GuildStatisticsCommandTest {
    private GuildStatisticsCommand guildStatisticsCommand;
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final GuildStatisticsRepository mockGuildStatisticsRepository = Mockito.mock(GuildStatisticsRepository.class);
    private final SlashCommandInteractionEvent mockEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final GuildData mockGuildData = Mockito.mock(GuildData.class);
    private final ReplyCallbackAction mockReplyCallbackAction = Mockito.mock(ReplyCallbackAction.class);

    @BeforeEach
    void setup() {
        guildStatisticsCommand = new GuildStatisticsCommand(mockMessageSender, mockGuildStatisticsRepository);
    }

    @Test
    void executeCommandWhenGuildIsInDbReturnsGuildStatistics() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        GuildStatisticsData guildStatisticsData = new GuildStatisticsData(1L, mockGuildData, 0,0);
        when(mockGuildStatisticsRepository.findByGuildId(1L)).thenReturn(Optional.of(guildStatisticsData));
        when(mockEvent.reply(anyString())).thenReturn(mockReplyCallbackAction);

        guildStatisticsCommand.executeCommand(mockEvent);

        verify(mockGuildStatisticsRepository).findByGuildId(1L);
        verify(mockEvent).reply(guildStatisticsData.toString());
        verify(mockReplyCallbackAction).queue();
    }

    @Test
    void executeCommandWhenGuildIsNotInDbReturnsErrorMessage() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildStatisticsRepository.findByGuildId(1L)).thenReturn(Optional.empty());
        when(mockEvent.reply(anyString())).thenReturn(mockReplyCallbackAction);

        guildStatisticsCommand.executeCommand(mockEvent);

        verify(mockGuildStatisticsRepository).findByGuildId(1L);
        verify(mockEvent).reply("DATABASE ERROR: Guild not found!");
        verify(mockReplyCallbackAction).queue();
    }
}