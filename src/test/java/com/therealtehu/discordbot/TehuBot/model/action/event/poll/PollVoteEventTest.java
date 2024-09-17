package com.therealtehu.discordbot.TehuBot.model.action.event.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.MemberRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.ClosePollCommand;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.service.poll.MessageReactionEventWithText;
import com.therealtehu.discordbot.TehuBot.service.poll.PollAnswerService;
import com.therealtehu.discordbot.TehuBot.service.poll.PollVoteHandler;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PollVoteEventTest {
    private PollVoteEvent pollVoteEvent;
    private final PollRepository mockPollRepository = Mockito.mock(PollRepository.class);
    private final PollVoteHandler mockPollVoteHandler = Mockito.mock(PollVoteHandler.class);
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final MessageReactionEventWithText mockEvent = Mockito.mock(MessageReactionEventWithText.class);
    private final PollData mockPollData = Mockito.mock(PollData.class);
    private final MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
    private final TextChannel mockTextChannel = Mockito.mock(TextChannel.class);

    @BeforeEach
    void setup() {
        pollVoteEvent = new PollVoteEvent(mockMessageSender, mockPollRepository, mockPollVoteHandler);
    }

    @Test
    void handleEventWhenPollIsInDbCallsVoteHandler() {
        when(mockEvent.getImmediateMessage()).thenReturn("__poll id:__ 987654321-1");
        when(mockPollRepository.findByPublicId("987654321-1")).thenReturn(Optional.of(mockPollData));

        pollVoteEvent.handle(mockEvent);

        verify(mockPollRepository).findByPublicId("987654321-1");
        verify(mockPollVoteHandler).handleVote(mockPollData, mockEvent);
    }

    @Test
    void handleEventWhenPollIsNotInDbSendsErrorMessage() {
        when(mockEvent.getImmediateMessage()).thenReturn("__poll id:__ 987654321-1");
        when(mockPollRepository.findByPublicId("987654321-1")).thenReturn(Optional.empty());
        when(mockEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        pollVoteEvent.handle(mockEvent);

        verify(mockPollRepository).findByPublicId("987654321-1");
        verifyNoInteractions(mockPollVoteHandler);
        verify(mockMessageSender).sendMessage(mockTextChannel, "ERROR: Could not find poll");
    }

    @Test
    void handleEventWhenPollIdIsNotFoundSendsErrorMessage() {
        when(mockEvent.getImmediateMessage()).thenReturn("Message with no poll id");
        when(mockEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        pollVoteEvent.handle(mockEvent);

        verifyNoInteractions(mockPollRepository);
        verifyNoInteractions(mockPollVoteHandler);
        verify(mockMessageSender).sendMessage(mockTextChannel, "ERROR: Could not find poll id");
    }
}