package com.therealtehu.discordbot.TehuBot.model.action.event.guild;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.MemberService;
import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerNewMemberEventTest {
    private ServerNewMemberEvent serverNewMemberEvent;
    @Mock
    private TenorGifService gifServiceMock;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private GuildRepository guildRepositoryMock;
    @Mock
    private GuildMemberJoinEvent guildMemberJoinEventMock;
    @Mock
    private Member memberMock;
    @Mock
    private Guild guildMock;
    @Mock
    private DefaultGuildChannelUnion channelUnionMock;
    @Mock
    private TextChannel textChannelMock;
    @Mock
    private MessageEmbed messageEmbedMock;
    @Mock
    private MemberService memberServiceMock;
    @Mock
    private GuildData guildDataMock;

    @BeforeEach
    void setup() {
        serverNewMemberEvent = new ServerNewMemberEvent(gifServiceMock, messageSenderMock,
                memberServiceMock, guildRepositoryMock);
    }

    @Test
    void handleServerNewMemberWhenGuildIsInDbGreetsMemberAndSavesThemToDb() {
        when(guildMemberJoinEventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");

        when(guildMemberJoinEventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getDefaultChannel()).thenReturn(channelUnionMock);
        when(channelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));
        when(guildDataMock.getBotChatChannelId()).thenReturn(30L);

        TextChannel otherTextChannel = Mockito.mock(TextChannel.class);
        when(guildMock.getTextChannelById(30L)).thenReturn(otherTextChannel);

        String message = "Member as mention just joined the channel! Say hi everyone!";

        when(gifServiceMock.getGifAsEmbed("Welcome")).thenReturn(messageEmbedMock);

        serverNewMemberEvent.handle(guildMemberJoinEventMock);

        verify(gifServiceMock).getGifAsEmbed("Welcome");
        verify(memberServiceMock).addNewMemberIfNotExists(memberMock);
        verify(messageSenderMock).sendMessageWithMessageEmbed(otherTextChannel, message, messageEmbedMock);
        verifyNoMoreInteractions(messageSenderMock);
    }

    @Test
    void handleServerNewMemberWhenGuildIsNotInDbSendsErrorMessage() {
        when(guildMemberJoinEventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getDefaultChannel()).thenReturn(channelUnionMock);
        when(channelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        serverNewMemberEvent.handle(guildMemberJoinEventMock);

        verifyNoInteractions(gifServiceMock);
        verifyNoInteractions(memberServiceMock);
        verify(messageSenderMock).sendMessage(textChannelMock, "ERROR: Guild not found in database!");
        verifyNoMoreInteractions(messageSenderMock);
    }
}