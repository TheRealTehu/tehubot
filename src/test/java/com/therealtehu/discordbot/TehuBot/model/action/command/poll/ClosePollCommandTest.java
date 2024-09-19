package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.OptionName;
import com.therealtehu.discordbot.TehuBot.service.poll.PollResultPrinter;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClosePollCommandTest {
    private ClosePollCommand closePollCommand;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private PollRepository pollRepositoryMock;
    @Mock
    private SlashCommandInteractionEvent eventMock;
    @Mock
    private Member memberMock;
    @Mock
    private OptionMapping optionMappingMock;
    @Mock
    private PollResultPrinter pollResultPrinterMock;
    @Mock
    private PollData pollDataMock;

    @BeforeEach
    void setup() {
        closePollCommand = new ClosePollCommand(pollRepositoryMock, messageSenderMock, pollResultPrinterMock);
    }

    @Test
    void closePollWhenPollIsOpenClosesPollAndSavesChangeToDbAndPrintsResult() {
        when(pollDataMock.isClosed()).thenReturn(false);

        closePollCommand.closePoll(pollDataMock);

        verify(pollRepositoryMock).save(pollDataMock);
        verify(pollResultPrinterMock).printResult(pollDataMock);
    }

    @Test
    void closePollWhenPollIsClosedDoesNothing() {
        when(pollDataMock.isClosed()).thenReturn(true);

        closePollCommand.closePoll(pollDataMock);

        verifyNoInteractions(pollRepositoryMock);
        verifyNoInteractions(pollResultPrinterMock);
    }

    @Test
    void executeCommandWhenMemberDoesNotHavePermissionThenPollDoesNotClose() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getPermissions()).thenReturn(EnumSet.of(Permission.UNKNOWN));

        closePollCommand.executeCommand(eventMock);

        verifyNoInteractions(pollRepositoryMock);
        verifyNoInteractions(pollResultPrinterMock);
    }

    @Test
    void executeCommandWhenPollIsNotInDbThenErrorMessageIsSent() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getPermissions()).thenReturn(EnumSet.of(Permission.MANAGE_EVENTS));
        when(eventMock.getOption(OptionName.POLL_ID_OPTION.getOptionName())).thenReturn(optionMappingMock);
        when((optionMappingMock.getAsString())).thenReturn("Not used poll id");
        when(pollRepositoryMock.findByPublicId("Not used poll id")).thenReturn(Optional.empty());

        closePollCommand.executeCommand(eventMock);

        verify(messageSenderMock).replyToEvent(eventMock, "ERROR: Could not find poll by id");
        verify(pollRepositoryMock, times(0)).save(any());
        verifyNoInteractions(pollResultPrinterMock);
    }

    @Test
    void executeCommandWhenPollIsInDbThenPollIsClosedAndResultIsPrinted() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getPermissions()).thenReturn(EnumSet.of(Permission.MANAGE_EVENTS));
        when(eventMock.getOption(OptionName.POLL_ID_OPTION.getOptionName())).thenReturn(optionMappingMock);
        when((optionMappingMock.getAsString())).thenReturn("poll id");
        PollData mockPollData = Mockito.mock(PollData.class);
        when(pollRepositoryMock.findByPublicId("poll id")).thenReturn(Optional.of(mockPollData));

        closePollCommand.executeCommand(eventMock);

        verify(pollRepositoryMock).findByPublicId("poll id");
        verify(mockPollData).setClosed(true);
        verify(pollRepositoryMock).save(mockPollData);
        verify(pollResultPrinterMock).printResult(mockPollData);
    }
}