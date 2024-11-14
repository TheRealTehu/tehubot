package com.therealtehu.discordbot.TehuBot.model.action.event.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.ClosePollCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollCloseSchedulerTest {
    private PollCloseScheduler pollCloseScheduler;
    @Mock
    private PollRepository pollRepositoryMock;
    @Mock
    private ClosePollCommand closePollCommandMock;
    @Mock
    private PollData pollDataMock;

    @BeforeEach
    void setup() {
        pollCloseScheduler = new PollCloseScheduler(pollRepositoryMock, closePollCommandMock);
    }

    @Test
    void closeExpiredPollsWhenHasOnePollThatIsOpenClosesThePoll() {
        when(pollRepositoryMock.findByIsClosedFalseAndDeadLineIsNotNullAndDeadLineBefore(any()))
                .thenReturn(List.of(pollDataMock));

        pollCloseScheduler.closeExpiredPolls();

        verify(closePollCommandMock).closePoll(pollDataMock);
    }

    @Test
    void closeExpiredPollsWhenHasTwoPollThatAreOpenClosesThePolls() {
        PollData mockPollData2 = Mockito.mock(PollData.class);
        when(pollRepositoryMock.findByIsClosedFalseAndDeadLineIsNotNullAndDeadLineBefore(any()))
                .thenReturn(List.of(pollDataMock, mockPollData2));

        pollCloseScheduler.closeExpiredPolls();

        verify(closePollCommandMock).closePoll(pollDataMock);
        verify(closePollCommandMock).closePoll(mockPollData2);
    }

    @Test
    void closeExpiredPollsWhenHasNoPollsOpenDoesNothing() {
        when(pollRepositoryMock.findByIsClosedFalseAndDeadLineIsNotNullAndDeadLineBefore(any()))
                .thenReturn(List.of());

        pollCloseScheduler.closeExpiredPolls();

        verifyNoInteractions(closePollCommandMock);
    }
}