package com.therealtehu.discordbot.TehuBot.service.poll;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.MemberRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.ClosePollCommand;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.requests.RestAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PollVoteHandlerTest {
    private PollVoteHandler pollVoteHandler;
    private final PollAnswerService mockPollAnswerService = Mockito.mock(PollAnswerService.class);
    private final ClosePollCommand mockClosePollCommand = Mockito.mock(ClosePollCommand.class);
    private final MemberRepository mockMemberRepository = Mockito.mock(MemberRepository.class);
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final PollData mockPollData = Mockito.mock(PollData.class);
    private final MessageReactionEventWithText mockEvent = Mockito.mock(MessageReactionEventWithText.class);
    private final MemberData mockMemberData = Mockito.mock(MemberData.class);
    private final PollAnswerData mockPollAnswerData = Mockito.mock(PollAnswerData.class);
    private final EmojiUnion mockEmojiUnion = Mockito.mock(EmojiUnion.class);

    @BeforeEach
    void setup() {
        pollVoteHandler = new PollVoteHandler(mockPollAnswerService, mockClosePollCommand, mockMemberRepository,
                mockMessageSender);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberCanVoteAndIsValidVoteSavesTheVote() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockPollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji"))
                .thenReturn(false);
        when(mockPollData.getMinimumRole()).thenReturn(null);
        when(mockPollData.getNumberOfVotesPerMember()).thenReturn(1);
        when(mockPollAnswerService.countVotes(mockPollData, mockMemberData)).thenReturn(0);
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");
        when(mockPollAnswerService.getPollAnswerData(mockPollData, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verify(mockPollAnswerData).addMember(mockMemberData);
        verify(mockPollAnswerService).saveAnswer(mockPollAnswerData);
        verifyNoInteractions(mockClosePollCommand);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberHasNeededPermissionAndIsValidVoteSavesTheVote() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockPollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji"))
                .thenReturn(false);

        when(mockPollData.getMinimumRole()).thenReturn("memberRole");
        Guild mockGuild = Mockito.mock(Guild.class);
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Role mockRole = Mockito.mock(Role.class);
        when(mockGuild.getRolesByName("memberRole", true)).thenReturn(List.of(mockRole));
        when(mockRole.getPosition()).thenReturn(1);

        Member mockMember = Mockito.mock(Member.class);
        when(mockEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getRoles()).thenReturn(List.of(mockRole));

        when(mockPollData.getNumberOfVotesPerMember()).thenReturn(1);
        when(mockPollAnswerService.countVotes(mockPollData, mockMemberData)).thenReturn(0);
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");
        when(mockPollAnswerService.getPollAnswerData(mockPollData, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verify(mockPollAnswerData).addMember(mockMemberData);
        verify(mockPollAnswerService).saveAnswer(mockPollAnswerData);
        verifyNoInteractions(mockClosePollCommand);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteBaseRolePermissionIsNeededAndIsValidVoteSavesTheVote() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockPollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji"))
                .thenReturn(false);

        when(mockPollData.getMinimumRole()).thenReturn("memberRole");
        Guild mockGuild = Mockito.mock(Guild.class);
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Role mockRole = Mockito.mock(Role.class);
        when(mockGuild.getRolesByName("memberRole", true)).thenReturn(List.of(mockRole));
        when(mockRole.getPosition()).thenReturn(-1);

        when(mockPollData.getNumberOfVotesPerMember()).thenReturn(1);
        when(mockPollAnswerService.countVotes(mockPollData, mockMemberData)).thenReturn(0);
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");
        when(mockPollAnswerService.getPollAnswerData(mockPollData, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verify(mockPollAnswerData).addMember(mockMemberData);
        verify(mockPollAnswerService).saveAnswer(mockPollAnswerData);
        verifyNoInteractions(mockClosePollCommand);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberDoesNotHaveNeededPermissionRemovesReactionAndDoesNotSaveVote() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockPollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji"))
                .thenReturn(false);

        when(mockPollData.getMinimumRole()).thenReturn("memberRole");
        Guild mockGuild = Mockito.mock(Guild.class);
        when(mockEvent.getGuild()).thenReturn(mockGuild);
        Role mockRole = Mockito.mock(Role.class);
        when(mockGuild.getRolesByName("memberRole", true)).thenReturn(List.of(mockRole));
        when(mockRole.getPosition()).thenReturn(1);

        Member mockMember = Mockito.mock(Member.class);
        when(mockEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getRoles()).thenReturn(List.of());

        MessageReaction mockMessageReaction = Mockito.mock(MessageReaction.class);
        when(mockEvent.getReaction()).thenReturn(mockMessageReaction);
        RestAction<Void> mockRestAction = Mockito.mock(RestAction.class);
        User mockUser = Mockito.mock(User.class);
        when(mockEvent.getUser()).thenReturn(mockUser);
        when(mockMessageReaction.removeReaction(mockUser)).thenReturn(mockRestAction);

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verifyNoInteractions(mockPollAnswerData);
        verify(mockPollAnswerService, times(0)).saveAnswer(mockPollAnswerData);
        verifyNoInteractions(mockClosePollCommand);
        verify(mockMessageReaction).removeReaction(mockUser);
        verify(mockRestAction).queue();
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberCanVoteAndIsNotValidVoteDoesNotSaveVote() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockPollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji"))
                .thenReturn(false);
        when(mockPollData.getMinimumRole()).thenReturn(null);
        when(mockPollData.getNumberOfVotesPerMember()).thenReturn(1);
        when(mockPollAnswerService.countVotes(mockPollData, mockMemberData)).thenReturn(0);
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");
        when(mockPollAnswerService.getPollAnswerData(mockPollData, "emoji"))
                .thenReturn(Optional.empty());

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verify(mockPollAnswerData, times(0)).addMember(mockMemberData);
        verify(mockPollAnswerService, times(0)).saveAnswer(mockPollAnswerData);
        verifyNoInteractions(mockClosePollCommand);
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberHasNoMoreVotesRemovesReactionAndDoesNotSaveVote() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockPollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji"))
                .thenReturn(false);
        when(mockPollData.getMinimumRole()).thenReturn(null);

        when(mockPollData.getNumberOfVotesPerMember()).thenReturn(1);
        when(mockPollAnswerService.countVotes(mockPollData, mockMemberData)).thenReturn(1);

        MessageReaction mockMessageReaction = Mockito.mock(MessageReaction.class);
        when(mockEvent.getReaction()).thenReturn(mockMessageReaction);
        RestAction<Void> mockRestAction = Mockito.mock(RestAction.class);
        User mockUser = Mockito.mock(User.class);
        when(mockEvent.getUser()).thenReturn(mockUser);
        when(mockMessageReaction.removeReaction(mockUser)).thenReturn(mockRestAction);

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verifyNoInteractions(mockPollAnswerData);
        verify(mockPollAnswerService, times(0)).saveAnswer(mockPollAnswerData);
        verifyNoInteractions(mockClosePollCommand);
        verify(mockMessageReaction).removeReaction(mockUser);
        verify(mockRestAction).queue();
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsAnonymousIsNotOverIsNotDoubleVoteMemberCanVoteAndIsValidVoteRemovesReactionsAndSavesTheVote() {
        when(mockPollData.isAnonymous()).thenReturn(true);
        MessageReaction mockMessageReaction = Mockito.mock(MessageReaction.class);
        when(mockEvent.getReaction()).thenReturn(mockMessageReaction);
        RestAction<Void> mockRestAction = Mockito.mock(RestAction.class);
        when(mockMessageReaction.clearReactions()).thenReturn(mockRestAction);

        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockPollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji"))
                .thenReturn(false);
        when(mockPollData.getMinimumRole()).thenReturn(null);
        when(mockPollData.getNumberOfVotesPerMember()).thenReturn(1);
        when(mockPollAnswerService.countVotes(mockPollData, mockMemberData)).thenReturn(0);
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");
        when(mockPollAnswerService.getPollAnswerData(mockPollData, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verify(mockMessageReaction).clearReactions();
        verify(mockRestAction).queue();
        verify(mockPollAnswerData).addMember(mockMemberData);
        verify(mockPollAnswerService).saveAnswer(mockPollAnswerData);
        verifyNoInteractions(mockClosePollCommand);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsAnonymousIsNotOverIsDoubleVoteRemovesReactionsRemovesVoteFromMemberAndSavesUpdatedState() {
        when(mockPollData.isAnonymous()).thenReturn(true);
        MessageReaction mockMessageReaction = Mockito.mock(MessageReaction.class);
        when(mockEvent.getReaction()).thenReturn(mockMessageReaction);
        RestAction<Void> mockRestAction = Mockito.mock(RestAction.class);
        when(mockMessageReaction.clearReactions()).thenReturn(mockRestAction);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockPollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji"))
                .thenReturn(true);
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verify(mockMessageReaction).clearReactions();
        verify(mockRestAction).queue();
        verify(mockPollAnswerData, times(0)).addMember(mockMemberData);
        verify(mockPollAnswerService, times(0)).saveAnswer(mockPollAnswerData);
        verifyNoInteractions(mockClosePollCommand);
        verify(mockPollAnswerService).removeVote(mockPollData, mockMemberData, "emoji");
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverAndMemberNotFoundSendsErrorMessageAndNothingIsSaved() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.empty());

        MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
        when(mockEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verify(mockMessageSender).sendMessage(mockTextChannel, "ERROR: Could not find user");
        verifyNoInteractions(mockPollAnswerData);
        verifyNoInteractions(mockPollAnswerService);
        verifyNoInteractions(mockClosePollCommand);
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousAndIsClosedSavesNothing() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(true);

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verifyNoInteractions(mockPollAnswerData);
        verifyNoInteractions(mockPollAnswerService);
        verifyNoInteractions(mockClosePollCommand);
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousHasDeadLineButIsNotOverIsNotDoubleVoteMemberCanVoteAndIsValidVoteSavesTheVote() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockPollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji"))
                .thenReturn(false);
        when(mockPollData.getMinimumRole()).thenReturn(null);
        when(mockPollData.getNumberOfVotesPerMember()).thenReturn(1);
        when(mockPollAnswerService.countVotes(mockPollData, mockMemberData)).thenReturn(0);
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");
        when(mockPollAnswerService.getPollAnswerData(mockPollData, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verify(mockPollAnswerData).addMember(mockMemberData);
        verify(mockPollAnswerService).saveAnswer(mockPollAnswerData);
        verifyNoInteractions(mockClosePollCommand);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousHasDeadLineAndIsOverSavesNothingAndClosesVote() {
        when(mockPollData.isAnonymous()).thenReturn(false);
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(5));

        boolean actual = pollVoteHandler.handleVote(mockPollData, mockEvent);

        verifyNoInteractions(mockPollAnswerData);
        verifyNoInteractions(mockPollAnswerService);
        verify(mockClosePollCommand).closePoll(mockPollData);
        assertFalse(actual);
    }

    @Test
    void removeVoteWhenVoteIsNotOverAndMemberHasVoteRemovesVote() {
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");
        when(mockPollAnswerService.getPollAnswerData(mockPollData, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));

        boolean actual = pollVoteHandler.removeVote(mockPollData, mockEvent);

        verify(mockMemberRepository).findByUserId(1L);
        verify(mockPollAnswerService).getPollAnswerData(mockPollData, "emoji");
        verify(mockPollAnswerData).removeMember(mockMemberData);
        verify(mockPollAnswerService).saveAnswer(mockPollAnswerData);
        assertTrue(actual);
    }

    @Test
    void removeVoteWhenVoteIsNotOverAndAnswerIsNotFoundDoesNothing() {
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");
        when(mockPollAnswerService.getPollAnswerData(mockPollData, "emoji"))
                .thenReturn(Optional.empty());

        boolean actual = pollVoteHandler.removeVote(mockPollData, mockEvent);

        verify(mockMemberRepository).findByUserId(1L);
        verify(mockPollAnswerService).getPollAnswerData(mockPollData, "emoji");
        verifyNoInteractions(mockPollAnswerData);
        verifyNoMoreInteractions(mockPollAnswerService);
        assertFalse(actual);
    }

    @Test
    void removeVoteWhenVoteIsNotOverAndMemberIsNotFoundSendsErrorMessageAndSavesNothing() {
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(null);
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.empty());

        MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
        when(mockEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        boolean actual = pollVoteHandler.removeVote(mockPollData, mockEvent);

        verify(mockMemberRepository).findByUserId(1L);
        verify(mockMessageSender).sendMessage(mockTextChannel, "ERROR: Could not find user");
        verifyNoInteractions(mockPollAnswerService);
        verifyNoInteractions(mockPollAnswerData);
        assertFalse(actual);
    }

    @Test
    void removeVoteWhenVoteIsClosedSavesNothing() {
        when(mockPollData.isClosed()).thenReturn(true);

        boolean actual = pollVoteHandler.removeVote(mockPollData, mockEvent);

        verifyNoInteractions(mockMemberRepository);
        verifyNoInteractions(mockPollAnswerService);
        verifyNoInteractions(mockPollAnswerData);
        assertFalse(actual);
    }

    @Test
    void removeVoteWhenVoteHasDeadlineButIsNotOverAndMemberHasVoteRemovesVote() {
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        when(mockEvent.getUserIdLong()).thenReturn(1L);
        when(mockMemberRepository.findByUserId(1L)).thenReturn(Optional.of(mockMemberData));
        when(mockEvent.getEmoji()).thenReturn(mockEmojiUnion);
        when(mockEmojiUnion.getAsReactionCode()).thenReturn("emoji");
        when(mockPollAnswerService.getPollAnswerData(mockPollData, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));

        boolean actual = pollVoteHandler.removeVote(mockPollData, mockEvent);

        verify(mockMemberRepository).findByUserId(1L);
        verify(mockPollAnswerService).getPollAnswerData(mockPollData, "emoji");
        verify(mockPollAnswerData).removeMember(mockMemberData);
        verify(mockPollAnswerService).saveAnswer(mockPollAnswerData);
        assertTrue(actual);
    }

    @Test
    void removeVoteWhenVoteHasDeadlineAndIsOverSavesNothingAndClosesVote() {
        when(mockPollData.isClosed()).thenReturn(false);
        when(mockPollData.getDeadLine()).thenReturn(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(5));

        boolean actual = pollVoteHandler.removeVote(mockPollData, mockEvent);

        verify(mockClosePollCommand).closePoll(mockPollData);
        verifyNoInteractions(mockMemberRepository);
        verifyNoInteractions(mockPollAnswerService);
        verifyNoInteractions(mockPollAnswerData);
        assertFalse(actual);
    }
}