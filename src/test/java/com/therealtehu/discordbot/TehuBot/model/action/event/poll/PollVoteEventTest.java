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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollVoteEventTest {
    private PollVoteEvent pollVoteEvent;
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
    @Mock
    private MessageChannelUnion messageChannelUnionMock;
    @Mock
    private TextChannel textChannelMock;

    @BeforeEach
    void setup() {
        pollVoteEvent = new PollVoteEvent(messageSenderMock, pollRepositoryMock, pollVoteHandlerMock);
    }

    @Test
    void handleEventWhenPollIsInDbCallsVoteHandler() {
        when(messageReactionEventWithTextMock.getImmediateMessage()).thenReturn("__poll id:__ 987654321-1");
        when(pollRepositoryMock.findByPublicId("987654321-1")).thenReturn(Optional.of(pollDataMock));

        pollVoteEvent.handle(messageReactionEventWithTextMock);

        verify(pollRepositoryMock).findByPublicId("987654321-1");
        verify(pollVoteHandlerMock).handleVote(pollDataMock, messageReactionEventWithTextMock);
    }

    @Test
    void handleEventWhenPollIsNotInDbSendsErrorMessage() {
        when(messageReactionEventWithTextMock.getImmediateMessage()).thenReturn("__poll id:__ 987654321-1");
        when(pollRepositoryMock.findByPublicId("987654321-1")).thenReturn(Optional.empty());
        when(messageReactionEventWithTextMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        pollVoteEvent.handle(messageReactionEventWithTextMock);

        verify(pollRepositoryMock).findByPublicId("987654321-1");
        verifyNoInteractions(pollVoteHandlerMock);
        verify(messageSenderMock).sendMessage(textChannelMock, "ERROR: Could not find poll");
    }

    @Test
    void handleEventWhenPollIdIsNotFoundSendsErrorMessage() {
        when(messageReactionEventWithTextMock.getImmediateMessage()).thenReturn("Message with no poll id");
        when(messageReactionEventWithTextMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        pollVoteEvent.handle(messageReactionEventWithTextMock);

        verifyNoInteractions(pollRepositoryMock);
        verifyNoInteractions(pollVoteHandlerMock);
        verify(messageSenderMock).sendMessage(textChannelMock, "ERROR: Could not find poll id");
    }
}