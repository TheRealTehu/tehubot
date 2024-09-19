package com.therealtehu.discordbot.TehuBot.model.action.event.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.ClosePollCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PollCloseSchedulerTest {
    private PollCloseScheduler pollCloseScheduler;
    private final PollRepository mockPollRepository = Mockito.mock(PollRepository.class);
    private final ClosePollCommand mockClosePollCommand = Mockito.mock(ClosePollCommand.class);
    private final PollData mockPollData = Mockito.mock(PollData.class);

    @BeforeEach
    void setup() {
        pollCloseScheduler = new PollCloseScheduler(mockPollRepository, mockClosePollCommand);
    }

    @Test
    void closeExpiredPollsWhenHasOnePollThatIsOpenClosesThePoll() {
        when(mockPollRepository.findByIsClosedFalseAndDeadLineIsNotNullAndDeadLineBefore(any()))
                .thenReturn(List.of(mockPollData));

        pollCloseScheduler.closeExpiredPolls();

        verify(mockClosePollCommand).closePoll(mockPollData);
    }

    @Test
    void closeExpiredPollsWhenHasTwoPollThatAreOpenClosesThePolls() {
        PollData mockPollData2 = Mockito.mock(PollData.class);
        when(mockPollRepository.findByIsClosedFalseAndDeadLineIsNotNullAndDeadLineBefore(any()))
                .thenReturn(List.of(mockPollData, mockPollData2));

        pollCloseScheduler.closeExpiredPolls();

        verify(mockClosePollCommand).closePoll(mockPollData);
        verify(mockClosePollCommand).closePoll(mockPollData2);
    }

    @Test
    void closeExpiredPollsWhenHasNoPollsOpenDoesNothing() {
        when(mockPollRepository.findByIsClosedFalseAndDeadLineIsNotNullAndDeadLineBefore(any()))
                .thenReturn(List.of());

        pollCloseScheduler.closeExpiredPolls();

        verifyNoInteractions(mockClosePollCommand);
    }
}