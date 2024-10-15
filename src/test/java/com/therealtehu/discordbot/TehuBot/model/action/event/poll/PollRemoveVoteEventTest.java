package com.therealtehu.discordbot.TehuBot.model.action.event.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.service.poll.MessageReactionEventWithText;
import com.therealtehu.discordbot.TehuBot.service.poll.PollVoteHandler;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollRemoveVoteEventTest {
    private PollRemoveVoteEvent removeVoteEvent;
    @Mock
    private PollRepository pollRepositoryMock;
    @Mock
    private PollVoteHandler pollVoteHandlerMock;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private MessageReactionEventWithText messageReactionEventWithTextMock;
    @Mock
    private PollData pollDataMock;

    @BeforeEach
    void setup() {
        removeVoteEvent = new PollRemoveVoteEvent(messageSenderMock, pollRepositoryMock, pollVoteHandlerMock);
    }

    @Test
    void handleEventWhenHasCorrectPollIdAndPollExistsCallsRemoveVote() {
        when(messageReactionEventWithTextMock.getImmediateMessage()).thenReturn("__poll id:__ 987654321-1");
        when(pollRepositoryMock.findByPublicId("987654321-1")).thenReturn(Optional.of(pollDataMock));

        removeVoteEvent.handle(messageReactionEventWithTextMock);

        verify(pollRepositoryMock).findByPublicId("987654321-1");
        verify(pollVoteHandlerMock).removeVote(pollDataMock, messageReactionEventWithTextMock);
    }

    @Test
    void handleEventWhenHasCorrectPollIdButPollDoesNotExistsSendErrorMessage() {
        when(messageReactionEventWithTextMock.getImmediateMessage()).thenReturn("__poll id:__ 987654321-1");
        when(pollRepositoryMock.findByPublicId("987654321-1")).thenReturn(Optional.empty());

        MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
        when(messageReactionEventWithTextMock.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        removeVoteEvent.handle(messageReactionEventWithTextMock);

        verify(pollRepositoryMock).findByPublicId("987654321-1");
        verify(messageSenderMock).sendMessage(mockTextChannel, "ERROR: Could not find poll");
        verifyNoInteractions(pollVoteHandlerMock);
    }

    @Test
    void handleEventWhenDoesNotHaveCorrectPollIdSendErrorMessage() {
        when(messageReactionEventWithTextMock.getImmediateMessage()).thenReturn("No poll id in message");
        MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
        when(messageReactionEventWithTextMock.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        removeVoteEvent.handle(messageReactionEventWithTextMock);

        verify(messageSenderMock).sendMessage(mockTextChannel, "ERROR: Could not find poll id");
        verifyNoInteractions(pollRepositoryMock);
        verifyNoInteractions(pollVoteHandlerMock);
    }
}