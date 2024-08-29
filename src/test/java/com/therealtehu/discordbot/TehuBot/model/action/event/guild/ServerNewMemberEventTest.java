package com.therealtehu.discordbot.TehuBot.model.action.event.guild;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.repository.MemberRepository;
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
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServerNewMemberEventTest {
    private ServerNewMemberEvent serverNewMemberEvent;

    private final TenorGifService mockGifService = Mockito.mock(TenorGifService.class);
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final GuildMemberJoinEvent mockMemberJoinEvent = Mockito.mock(GuildMemberJoinEvent.class);
    private final Member mockMember = Mockito.mock(Member.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final DefaultGuildChannelUnion mockChannelUnion = Mockito.mock(DefaultGuildChannelUnion.class);
    private final TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
    private final MessageEmbed mockMessageEmbed = Mockito.mock(MessageEmbed.class);
    private final MemberRepository mockMemberRepository = Mockito.mock(MemberRepository.class);

    @BeforeEach
    void setup() {
        serverNewMemberEvent = new ServerNewMemberEvent(mockGifService, mockMessageSender, mockMemberRepository);
    }

    @Test
    void handleServerNewMemberWhenMemberIsNotInDbGreetsMemberAndSavesThemToDb() {
        when(mockMemberJoinEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockMemberJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockChannelUnion);
        when(mockChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        String message = "Member as mention just joined the channel! Say hi everyone!";

        when(mockGifService.getGifAsEmbed("Welcome")).thenReturn(mockMessageEmbed);

        when(mockMember.getIdLong()).thenReturn(1L);
        when(mockMemberRepository.existsByUserId(1L)).thenReturn(false);

        serverNewMemberEvent.handle(mockMemberJoinEvent);

        MemberData expectedMemberData = new MemberData();
        expectedMemberData.setUserId(1L);

        verify(mockGifService).getGifAsEmbed("Welcome");
        verify(mockMessageSender).sendMessageWithMessageEmbed(mockTextChannel, message, mockMessageEmbed);
        verify(mockMemberRepository).save(expectedMemberData);
    }

    @Test
    void handleServerNewMemberWhenMemberIsInDbGreetsMemberAndDoesNotSavesThemToDb() {
        when(mockMemberJoinEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockMemberJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockChannelUnion);
        when(mockChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        String message = "Member as mention just joined the channel! Say hi everyone!";

        when(mockGifService.getGifAsEmbed("Welcome")).thenReturn(mockMessageEmbed);

        when(mockMember.getIdLong()).thenReturn(1L);
        when(mockMemberRepository.existsByUserId(1L)).thenReturn(true);

        serverNewMemberEvent.handle(mockMemberJoinEvent);

        verify(mockGifService).getGifAsEmbed("Welcome");
        verify(mockMessageSender).sendMessageWithMessageEmbed(mockTextChannel, message, mockMessageEmbed);
        verify(mockMemberRepository, times(0)).save(any());
    }
}