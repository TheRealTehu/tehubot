package com.therealtehu.discordbot.TehuBot.model.action.event.guild;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.model.action.event.guild.server_join.ServerJoinEvent;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

class ServerJoinEventTest {
    private static final String GREETING_TEXT = "Hey! I'm TehuBot! I'm new on this server, a server admin please go through my initial setup!\n Where should I post?\n";
    private ServerJoinEvent serverJoinEvent;
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final GuildJoinEvent mockGuildJoinEvent = Mockito.mock(GuildJoinEvent.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final DefaultGuildChannelUnion mockDefaultGuildChannelUnion = Mockito.mock(DefaultGuildChannelUnion.class);
    private final TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
    private final MessageCreateBuilder mockMessageCreateBuilder = Mockito.mock(MessageCreateBuilder.class);
    private final MessageCreateData mockMessageCreateData = Mockito.mock(MessageCreateData.class);
    private final GuildRepository mockGuildRepository = Mockito.mock(GuildRepository.class);

    private final EntitySelectMenu channelDropDown = EntitySelectMenu
            .create("choose-bot-channel", EntitySelectMenu.SelectTarget.CHANNEL)
            .setChannelTypes(ChannelType.TEXT)
            .setRequiredRange(1, 1)
            .build();

    @Test
    void handleServerJoinWhenGuildIsNotInDbSavesGuildAndReturnsDropDownMenu() {
        serverJoinEvent = new ServerJoinEvent(mockMessageSender, mockMessageCreateBuilder, mockGuildRepository);

        when(mockGuildJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockDefaultGuildChannelUnion);
        when(mockDefaultGuildChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(Optional.empty());

        GuildData expectedGuildData = new GuildData();
        expectedGuildData.setGuildId(1L);

        when(mockMessageCreateBuilder.addContent(GREETING_TEXT)).thenReturn(mockMessageCreateBuilder);
        when(mockMessageCreateBuilder.addActionRow(channelDropDown)).thenReturn(mockMessageCreateBuilder);
        when(mockMessageCreateBuilder.build()).thenReturn(mockMessageCreateData);

        serverJoinEvent.handle(mockGuildJoinEvent);

        verify(mockGuildRepository).save(expectedGuildData);
        verify(mockMessageSender).sendMessage(mockTextChannel, mockMessageCreateData);
    }

    @Test
    void handleServerJoinWhenGuildIsInDbReturnsMessageThatGuildIsFound() {
        serverJoinEvent = new ServerJoinEvent(mockMessageSender, mockMessageCreateBuilder, mockGuildRepository);

        when(mockGuildJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockDefaultGuildChannelUnion);
        when(mockDefaultGuildChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        when(mockGuild.getIdLong()).thenReturn(1L);
        GuildData guildData = new GuildData();
        guildData.setGuildId(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(Optional.of(guildData));

        serverJoinEvent.handle(mockGuildJoinEvent);

        verify(mockGuildRepository, times(0)).save(any());
        verify(mockMessageSender).sendMessage(mockTextChannel, "Welcome back! Previous setup loaded!");
    }
}