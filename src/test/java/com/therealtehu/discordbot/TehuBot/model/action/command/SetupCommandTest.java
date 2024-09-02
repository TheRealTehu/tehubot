package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.MemberService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SetupCommandTest {

    private SetupCommand setupCommand;
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final GuildRepository mockGuildRepository = Mockito.mock(GuildRepository.class);
    private final SlashCommandInteractionEvent mockEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final DefaultGuildChannelUnion mockDefaultGuildChannelUnion = Mockito.mock(DefaultGuildChannelUnion.class);
    private final ReplyCallbackAction mockReply = Mockito.mock(ReplyCallbackAction.class);
    private final MemberService mockMemberService = Mockito.mock(MemberService.class);

    @BeforeEach
    void setup() {
        setupCommand = new SetupCommand(mockMessageSender, mockGuildRepository, mockMemberService);
    }

    @Test
    void executeCommandSavesGuildAndMemberToDbAndRepliesToEvent() {
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuild.getDefaultChannel()).thenReturn(mockDefaultGuildChannelUnion);
        when(mockDefaultGuildChannelUnion.getIdLong()).thenReturn(100L);
        when(mockEvent.reply("Guild saved to DB")).thenReturn(mockReply);

        setupCommand.executeCommand(mockEvent);

        GuildData expectedGuildData = new GuildData(1L,100L);

        verify(mockGuildRepository).save(expectedGuildData);
        verify(mockEvent).reply("Guild saved to DB");
        verify(mockMemberService).addMembersFromGuild(mockGuild);
        verify(mockReply).queue();
    }
}