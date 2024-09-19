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
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

class PollRemoveVoteEventTest {
    private PollRemoveVoteEvent removeVoteEvent;
    private final PollRepository mockPollRepository = Mockito.mock(PollRepository.class);
    private final PollVoteHandler mockPollVoteHandler = Mockito.mock(PollVoteHandler.class);
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final MessageReactionEventWithText mockEvent = Mockito.mock(MessageReactionEventWithText.class);
    private final PollData mockPollData = Mockito.mock(PollData.class);

    @BeforeEach
    void setup() {
        removeVoteEvent = new PollRemoveVoteEvent(mockMessageSender, mockPollRepository, mockPollVoteHandler);
    }

    @Test
    void handleEventWhenHasCorrectPollIdAndPollExistsCallsRemoveVote() {
        when(mockEvent.getImmediateMessage()).thenReturn("__poll id:__ 987654321-1");
        when(mockPollRepository.findByPublicId("987654321-1")).thenReturn(Optional.of(mockPollData));

        removeVoteEvent.handle(mockEvent);

        verify(mockPollRepository).findByPublicId("987654321-1");
        verify(mockPollVoteHandler).removeVote(mockPollData, mockEvent);
    }

    @Test
    void handleEventWhenHasCorrectPollIdButPollDoesNotExistsSendErrorMessage() {
        when(mockEvent.getImmediateMessage()).thenReturn("__poll id:__ 987654321-1");
        when(mockPollRepository.findByPublicId("987654321-1")).thenReturn(Optional.empty());

        MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
        when(mockEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        removeVoteEvent.handle(mockEvent);

        verify(mockPollRepository).findByPublicId("987654321-1");
        verify(mockMessageSender).sendMessage(mockTextChannel, "ERROR: Could not find poll");
        verifyNoInteractions(mockPollVoteHandler);
    }

    @Test
    void handleEventWhenDoesNotHaveCorrectPollIdSendErrorMessage() {
        when(mockEvent.getImmediateMessage()).thenReturn("No poll id in message");
        MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
        when(mockEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        removeVoteEvent.handle(mockEvent);

        verify(mockMessageSender).sendMessage(mockTextChannel, "ERROR: Could not find poll id");
        verifyNoInteractions(mockPollRepository);
        verifyNoInteractions(mockPollVoteHandler);
    }
}