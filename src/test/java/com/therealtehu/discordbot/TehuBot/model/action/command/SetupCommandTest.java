package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.MemberService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SetupCommandTest {

    private SetupCommand setupCommand;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private GuildRepository guildRepositoryMock;
    @Mock
    private SlashCommandInteractionEvent eventMock;
    @Mock
    private Guild guildMock;
    @Mock
    private DefaultGuildChannelUnion defaultGuildChannelUnionMock;
    @Mock
    private MemberService memberServiceMock;
    @Mock
    private Member memberMock;

    @BeforeEach
    void setup() {
        setupCommand = new SetupCommand(messageSenderMock, guildRepositoryMock, memberServiceMock);
    }

    @Test
    void executeCommandWhenUserHasPermissionSavesGuildAndMemberToDbAndRepliesToEvent() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MANAGE_SERVER)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildMock.getDefaultChannel()).thenReturn(defaultGuildChannelUnionMock);
        when(defaultGuildChannelUnionMock.getIdLong()).thenReturn(100L);

        setupCommand.executeCommand(eventMock);

        GuildData expectedGuildData = new GuildData(1L,100L);

        verify(guildRepositoryMock).save(expectedGuildData);
        verify(memberServiceMock).addMembersFromGuild(guildMock);
        verify(messageSenderMock).reply(eventMock, "Guild saved to DB");
    }

    @Test
    void executeCommandWhenUserDoesNotHavePermissionSendErrorMessage() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MANAGE_SERVER)).thenReturn(false);

        setupCommand.executeCommand(eventMock);

        verifyNoInteractions(guildRepositoryMock);
        verifyNoInteractions(memberServiceMock);
        verify(messageSenderMock).reply(eventMock, "ERROR: Doesn't have permission to setup guild!");
    }
}