package com.therealtehu.discordbot.TehuBot.model.action.event.guild.server_join;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.MemberService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerJoinEventTest {
    private static final String GREETING_TEXT = "Hey! I'm TehuBot! I'm new on this server, a server admin please go through my initial setup!\n Where should I post?\n";
    private ServerJoinEvent serverJoinEvent;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private GuildJoinEvent guildJoinEventMock;
    @Mock
    private Guild guildMock;
    @Mock
    private DefaultGuildChannelUnion defaultGuildChannelUnionMock;
    @Mock
    private TextChannel textChannelMock;
    @Mock
    private MessageCreateBuilder messageCreateBuilderMock;
    @Mock
    private MessageCreateData messageCreateDataMock;
    @Mock
    private GuildRepository guildRepositoryMock;
    @Mock
    private MemberService memberServiceMock;
    private final EntitySelectMenu channelDropDown = EntitySelectMenu
            .create("choose-bot-channel", EntitySelectMenu.SelectTarget.CHANNEL)
            .setChannelTypes(ChannelType.TEXT)
            .setRequiredRange(1, 1)
            .build();

    @BeforeEach
    void setup() {
        serverJoinEvent = new ServerJoinEvent(messageSenderMock, messageCreateBuilderMock,
                memberServiceMock, guildRepositoryMock);
    }

    @Test
    void handleServerJoinWhenGuildIsNotInDbSavesGuildAndReturnsDropDownMenu() {
        when(guildJoinEventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getDefaultChannel()).thenReturn(defaultGuildChannelUnionMock);
        when(defaultGuildChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        GuildData expectedGuildData = new GuildData();
        expectedGuildData.setId(1L);

        when(messageCreateBuilderMock.addContent(GREETING_TEXT)).thenReturn(messageCreateBuilderMock);
        when(messageCreateBuilderMock.addActionRow(channelDropDown)).thenReturn(messageCreateBuilderMock);
        when(messageCreateBuilderMock.build()).thenReturn(messageCreateDataMock);

        serverJoinEvent.handle(guildJoinEventMock);

        verify(guildRepositoryMock).save(expectedGuildData);
        verify(messageSenderMock).sendMessage(textChannelMock, messageCreateDataMock);
        verify(memberServiceMock).addMembersFromGuild(guildMock);
    }
    @Test
    void handleServerJoinWhenGuildIsInDbReturnsMessageThatGuildIsFound() {
        when(guildJoinEventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getDefaultChannel()).thenReturn(defaultGuildChannelUnionMock);
        when(defaultGuildChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(guildMock.getIdLong()).thenReturn(1L);
        GuildData guildData = new GuildData();
        guildData.setId(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildData));

        serverJoinEvent.handle(guildJoinEventMock);

        verify(guildRepositoryMock, times(0)).save(any());
        verify(messageSenderMock).sendMessage(textChannelMock, "Welcome back! Previous setup loaded!");
        verify(memberServiceMock).addMembersFromGuild(guildMock);
    }
}