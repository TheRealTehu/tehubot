package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.OptionName;
import com.therealtehu.discordbot.TehuBot.model.action.event.poll.PollResultPrinter;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.EnumSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ClosePollCommandTest {
    private ClosePollCommand closePollCommand;
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final PollRepository mockPollRepository = Mockito.mock(PollRepository.class);
    private final SlashCommandInteractionEvent mockEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final Member mockMember = Mockito.mock(Member.class);
    private final OptionMapping mockOptionMapping = Mockito.mock(OptionMapping.class);
    private final PollResultPrinter mockPollResultPrinter = Mockito.mock(PollResultPrinter.class);
    private final PollData mockPollData = Mockito.mock(PollData.class);

    @BeforeEach
    void setup() {
        closePollCommand = new ClosePollCommand(mockPollRepository, mockMessageSender, mockPollResultPrinter);
    }

    @Test
    void closePollWhenPollIsOpenClosesPollAndSavesChangeToDbAndPrintsResult() {
        when(mockPollData.isClosed()).thenReturn(false);

        closePollCommand.closePoll(mockPollData);

        verify(mockPollRepository).save(mockPollData);
        verify(mockPollResultPrinter).printResult(mockPollData);
    }

    @Test
    void closePollWhenPollIsClosedDoesNothing() {
        when(mockPollData.isClosed()).thenReturn(true);

        closePollCommand.closePoll(mockPollData);

        verifyNoInteractions(mockPollRepository);
        verifyNoInteractions(mockPollResultPrinter);
    }

    @Test
    void executeCommandWhenMemberDoesNotHavePermissionThenPollDoesNotClose() {
        when(mockEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getPermissions()).thenReturn(EnumSet.of(Permission.UNKNOWN));

        closePollCommand.executeCommand(mockEvent);

        verifyNoInteractions(mockPollRepository);
        verifyNoInteractions(mockPollResultPrinter);
    }

    @Test
    void executeCommandWhenPollIsNotInDbThenErrorMessageIsSent() {
        when(mockEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getPermissions()).thenReturn(EnumSet.of(Permission.MANAGE_EVENTS));
        when(mockEvent.getOption(OptionName.POLL_ID_OPTION.getOptionName())).thenReturn(mockOptionMapping);
        when((mockOptionMapping.getAsString())).thenReturn("Not used poll id");
        when(mockPollRepository.findByPublicId("Not used poll id")).thenReturn(Optional.empty());

        closePollCommand.executeCommand(mockEvent);

        verify(mockMessageSender).replyToEvent(mockEvent, "ERROR: Could not find poll by id");
        verify(mockPollRepository, times(0)).save(any());
        verifyNoInteractions(mockPollResultPrinter);
    }

    @Test
    void executeCommandWhenPollIsInDbThenPollIsClosedAndResultIsPrinted() {
        when(mockEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getPermissions()).thenReturn(EnumSet.of(Permission.MANAGE_EVENTS));
        when(mockEvent.getOption(OptionName.POLL_ID_OPTION.getOptionName())).thenReturn(mockOptionMapping);
        when((mockOptionMapping.getAsString())).thenReturn("poll id");
        PollData mockPollData = Mockito.mock(PollData.class);
        when(mockPollRepository.findByPublicId("poll id")).thenReturn(Optional.of(mockPollData));

        closePollCommand.executeCommand(mockEvent);

        verify(mockPollRepository).findByPublicId("poll id");
        verify(mockPollData).setClosed(true);
        verify(mockPollRepository).save(mockPollData);
        verify(mockPollResultPrinter).printResult(mockPollData);
    }
}