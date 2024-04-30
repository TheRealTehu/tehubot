package com.therealtehu.discordbot.TehuBot.model.action.event.guild;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setup() {
        serverNewMemberEvent = new ServerNewMemberEvent(mockGifService, mockMessageSender);
    }

    @Test
    void handle() {
        when(mockMemberJoinEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockMemberJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockChannelUnion);
        when(mockChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        String message = "Member as mention just joined the channel! Say hi everyone!";

        when(mockGifService.getGifAsEmbed("Welcome")).thenReturn(mockMessageEmbed);

        serverNewMemberEvent.handle(mockMemberJoinEvent);

        verify(mockGifService).getGifAsEmbed("Welcome");
        verify(mockMessageSender).sendMessageWithMessageEmbed(mockTextChannel, message, mockMessageEmbed);
    }
}