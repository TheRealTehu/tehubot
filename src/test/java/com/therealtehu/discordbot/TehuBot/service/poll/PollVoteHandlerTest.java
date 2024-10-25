package com.therealtehu.discordbot.TehuBot.service.poll;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.ClosePollCommand;
import com.therealtehu.discordbot.TehuBot.service.MemberService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.requests.RestAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollVoteHandlerTest {
    private PollVoteHandler pollVoteHandler;
    @Mock
    private PollAnswerService pollAnswerServiceMock;
    @Mock
    private ClosePollCommand closePollCommandMock;
    @Mock
    private MemberService memberServiceMock;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private PollData pollDataMock;
    @Mock
    private MessageReactionEventWithText reactionEventWithTextMock;
    @Mock
    private MemberData memberDataMock;
    @Mock
    private EmojiUnion emojiUnionMock;


    @BeforeEach
    void setup() {
        pollVoteHandler = new PollVoteHandler(pollAnswerServiceMock, closePollCommandMock, memberServiceMock,
                messageSenderMock);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberCanVoteAndIsValidVoteSavesTheVote() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);
        when(pollDataMock.getMinimumRole()).thenReturn(null);
        when(pollDataMock.getNumberOfVotesPerMember()).thenReturn(1);
        when(pollAnswerServiceMock.countVotes(pollDataMock, memberDataMock)).thenReturn(0);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");
        when(pollAnswerServiceMock.addVote(pollDataMock, "emoji", memberDataMock))
                .thenReturn(true);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verify(pollAnswerServiceMock).addVote(pollDataMock, "emoji", memberDataMock);
        verifyNoInteractions(closePollCommandMock);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberHasNeededPermissionAndIsValidVoteSavesTheVote() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);

        when(pollDataMock.getMinimumRole()).thenReturn("memberRole");
        Guild mockGuild = Mockito.mock(Guild.class);
        when(reactionEventWithTextMock.getGuild()).thenReturn(mockGuild);
        Role mockRole = Mockito.mock(Role.class);
        when(mockGuild.getRolesByName("memberRole", true)).thenReturn(List.of(mockRole));
        when(mockRole.getPosition()).thenReturn(1);

        Member mockMember = Mockito.mock(Member.class);
        when(reactionEventWithTextMock.getMember()).thenReturn(mockMember);
        when(mockMember.getRoles()).thenReturn(List.of(mockRole));

        when(pollDataMock.getNumberOfVotesPerMember()).thenReturn(1);
        when(pollAnswerServiceMock.countVotes(pollDataMock, memberDataMock)).thenReturn(0);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");
        when(pollAnswerServiceMock.addVote(pollDataMock, "emoji", memberDataMock))
                .thenReturn(true);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verify(pollAnswerServiceMock).addVote(pollDataMock, "emoji", memberDataMock);
        verifyNoInteractions(closePollCommandMock);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteBaseRolePermissionIsNeededAndIsValidVoteSavesTheVote() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);

        when(pollDataMock.getMinimumRole()).thenReturn("memberRole");
        Guild mockGuild = Mockito.mock(Guild.class);
        when(reactionEventWithTextMock.getGuild()).thenReturn(mockGuild);
        Role mockRole = Mockito.mock(Role.class);
        when(mockGuild.getRolesByName("memberRole", true)).thenReturn(List.of(mockRole));
        when(mockRole.getPosition()).thenReturn(-1);

        when(pollDataMock.getNumberOfVotesPerMember()).thenReturn(1);
        when(pollAnswerServiceMock.countVotes(pollDataMock, memberDataMock)).thenReturn(0);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");
        when(pollAnswerServiceMock.addVote(pollDataMock, "emoji", memberDataMock))
                .thenReturn(true);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verify(pollAnswerServiceMock).addVote(pollDataMock, "emoji", memberDataMock);
        verifyNoInteractions(closePollCommandMock);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberDoesNotHaveNeededPermissionRemovesReactionAndDoesNotSaveVote() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);

        when(pollDataMock.getMinimumRole()).thenReturn("memberRole");
        Guild mockGuild = Mockito.mock(Guild.class);
        when(reactionEventWithTextMock.getGuild()).thenReturn(mockGuild);
        Role mockRole = Mockito.mock(Role.class);
        when(mockGuild.getRolesByName("memberRole", true)).thenReturn(List.of(mockRole));
        when(mockRole.getPosition()).thenReturn(1);

        Member mockMember = Mockito.mock(Member.class);
        when(reactionEventWithTextMock.getMember()).thenReturn(mockMember);
        when(mockMember.getRoles()).thenReturn(List.of());

        MessageReaction mockMessageReaction = Mockito.mock(MessageReaction.class);
        when(reactionEventWithTextMock.getReaction()).thenReturn(mockMessageReaction);
        RestAction<Void> mockRestAction = Mockito.mock(RestAction.class);
        User mockUser = Mockito.mock(User.class);
        when(reactionEventWithTextMock.getUser()).thenReturn(mockUser);
        when(mockMessageReaction.removeReaction(mockUser)).thenReturn(mockRestAction);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verifyNoInteractions(pollAnswerServiceMock);
        verifyNoInteractions(closePollCommandMock);
        verify(mockMessageReaction).removeReaction(mockUser);
        verify(mockRestAction).queue();
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberCanVoteAndIsNotValidVoteDoesNotSaveVote() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);
        when(pollDataMock.getMinimumRole()).thenReturn(null);
        when(pollDataMock.getNumberOfVotesPerMember()).thenReturn(1);
        when(pollAnswerServiceMock.countVotes(pollDataMock, memberDataMock)).thenReturn(0);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");
        when(pollAnswerServiceMock.addVote(pollDataMock, "emoji", memberDataMock))
                .thenReturn(false);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verify(pollAnswerServiceMock).addVote(pollDataMock, "emoji", memberDataMock);
        verifyNoInteractions(closePollCommandMock);
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverIsNotDoubleVoteMemberHasNoMoreVotesRemovesReactionAndDoesNotSaveVote() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);
        when(pollDataMock.getMinimumRole()).thenReturn(null);

        when(pollDataMock.getNumberOfVotesPerMember()).thenReturn(1);
        when(pollAnswerServiceMock.countVotes(pollDataMock, memberDataMock)).thenReturn(1);

        MessageReaction mockMessageReaction = Mockito.mock(MessageReaction.class);
        when(reactionEventWithTextMock.getReaction()).thenReturn(mockMessageReaction);
        RestAction<Void> mockRestAction = Mockito.mock(RestAction.class);
        User mockUser = Mockito.mock(User.class);
        when(reactionEventWithTextMock.getUser()).thenReturn(mockUser);
        when(mockMessageReaction.removeReaction(mockUser)).thenReturn(mockRestAction);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verify(pollAnswerServiceMock, times(0)).addVote(any(), any(), any());
        verifyNoInteractions(closePollCommandMock);
        verify(mockMessageReaction).removeReaction(mockUser);
        verify(mockRestAction).queue();
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsAnonymousIsNotOverIsNotDoubleVoteMemberCanVoteAndIsValidVoteRemovesReactionsAndSavesTheVote() {
        when(pollDataMock.isAnonymous()).thenReturn(true);
        MessageReaction mockMessageReaction = Mockito.mock(MessageReaction.class);
        when(reactionEventWithTextMock.getReaction()).thenReturn(mockMessageReaction);
        RestAction<Void> mockRestAction = Mockito.mock(RestAction.class);
        when(mockMessageReaction.clearReactions()).thenReturn(mockRestAction);

        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);
        when(pollAnswerServiceMock.voteExistsForMember(memberDataMock, pollDataMock, "emoji"))
                .thenReturn(false);
        when(pollDataMock.getMinimumRole()).thenReturn(null);
        when(pollDataMock.getNumberOfVotesPerMember()).thenReturn(1);
        when(pollAnswerServiceMock.countVotes(pollDataMock, memberDataMock)).thenReturn(0);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");
        when(pollAnswerServiceMock.addVote(pollDataMock, "emoji", memberDataMock))
                .thenReturn(true);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verify(mockMessageReaction).clearReactions();
        verify(mockRestAction).queue();
        verify(pollAnswerServiceMock).addVote(pollDataMock, "emoji", memberDataMock);
        verifyNoInteractions(closePollCommandMock);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsAnonymousIsNotOverIsDoubleVoteRemovesReactionsRemovesVoteFromMemberAndSavesUpdatedState() {
        when(pollDataMock.isAnonymous()).thenReturn(true);
        MessageReaction mockMessageReaction = Mockito.mock(MessageReaction.class);
        when(reactionEventWithTextMock.getReaction()).thenReturn(mockMessageReaction);
        RestAction<Void> mockRestAction = Mockito.mock(RestAction.class);
        when(mockMessageReaction.clearReactions()).thenReturn(mockRestAction);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);
        when(pollAnswerServiceMock.voteExistsForMember(memberDataMock, pollDataMock, "emoji"))
                .thenReturn(true);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verify(mockMessageReaction).clearReactions();
        verify(mockRestAction).queue();
        verify(pollAnswerServiceMock, times(0)).addVote(any(), any(), any());
        verifyNoInteractions(closePollCommandMock);
        verify(pollAnswerServiceMock).removeVote(pollDataMock, memberDataMock, "emoji");
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousIsNotOverAndMemberNotFoundSendsErrorMessageAndNothingIsSaved() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenThrow(NoSuchElementException.class);

        MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
        when(reactionEventWithTextMock.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verify(messageSenderMock).sendMessage(mockTextChannel, "ERROR: Could not find user");
        verifyNoInteractions(pollAnswerServiceMock);
        verifyNoInteractions(closePollCommandMock);
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousAndIsClosedSavesNothing() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(true);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verifyNoInteractions(pollAnswerServiceMock);
        verifyNoInteractions(closePollCommandMock);
        assertFalse(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousHasDeadLineButIsNotOverIsNotDoubleVoteMemberCanVoteAndIsValidVoteSavesTheVote() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);
        when(pollDataMock.getMinimumRole()).thenReturn(null);
        when(pollDataMock.getNumberOfVotesPerMember()).thenReturn(1);
        when(pollAnswerServiceMock.countVotes(pollDataMock, memberDataMock)).thenReturn(0);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");
        when(pollAnswerServiceMock.addVote(pollDataMock, "emoji", memberDataMock))
                .thenReturn(true);

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verify(pollAnswerServiceMock).addVote(pollDataMock, "emoji", memberDataMock);
        verifyNoInteractions(closePollCommandMock);
        assertTrue(actual);
    }

    @Test
    void handleVoteWhenVoteIsNotAnonymousHasDeadLineAndIsOverSavesNothingAndClosesVote() {
        when(pollDataMock.isAnonymous()).thenReturn(false);
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(5));

        boolean actual = pollVoteHandler.handleVote(pollDataMock, reactionEventWithTextMock);

        verifyNoInteractions(pollAnswerServiceMock);
        verify(closePollCommandMock).closePoll(pollDataMock);
        assertFalse(actual);
    }

    @Test
    void removeVoteWhenVoteIsNotOverAndMemberHasVoteRemovesVote() {
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");
        when(pollAnswerServiceMock.removeVote(pollDataMock, memberDataMock, "emoji"))
                .thenReturn(true);

        boolean actual = pollVoteHandler.removeVote(pollDataMock, reactionEventWithTextMock);

        verify(memberServiceMock).getMemberData(1L);
        verify(pollAnswerServiceMock).removeVote(pollDataMock, memberDataMock, "emoji");
        assertTrue(actual);
    }

    @Test
    void removeVoteWhenVoteIsNotOverAndAnswerIsNotFoundDoesNothing() {
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");
        when(pollAnswerServiceMock.removeVote(pollDataMock, memberDataMock, "emoji"))
                .thenReturn(false);

        boolean actual = pollVoteHandler.removeVote(pollDataMock, reactionEventWithTextMock);

        verify(memberServiceMock).getMemberData(1L);
        verify(pollAnswerServiceMock).removeVote(pollDataMock, memberDataMock, "emoji");
        assertFalse(actual);
    }

    @Test
    void removeVoteWhenVoteIsNotOverAndMemberIsNotFoundSendsErrorMessageAndSavesNothing() {
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(null);
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenThrow(NoSuchElementException.class);

        MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
        TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
        when(reactionEventWithTextMock.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        boolean actual = pollVoteHandler.removeVote(pollDataMock, reactionEventWithTextMock);

        verify(memberServiceMock).getMemberData(1L);
        verify(messageSenderMock).sendMessage(mockTextChannel, "ERROR: Could not find user");
        verifyNoInteractions(pollAnswerServiceMock);
        assertFalse(actual);
    }

    @Test
    void removeVoteWhenVoteIsClosedSavesNothing() {
        when(pollDataMock.isClosed()).thenReturn(true);

        boolean actual = pollVoteHandler.removeVote(pollDataMock, reactionEventWithTextMock);

        verifyNoInteractions(memberServiceMock);
        verifyNoInteractions(pollAnswerServiceMock);
        assertFalse(actual);
    }

    @Test
    void removeVoteWhenVoteHasDeadlineButIsNotOverAndMemberHasVoteRemovesVote() {
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        when(reactionEventWithTextMock.getUserIdLong()).thenReturn(1L);
        when(memberServiceMock.getMemberData(1L)).thenReturn(memberDataMock);
        when(reactionEventWithTextMock.getEmoji()).thenReturn(emojiUnionMock);
        when(emojiUnionMock.getAsReactionCode()).thenReturn("emoji");
        when(pollAnswerServiceMock.removeVote(pollDataMock, memberDataMock, "emoji"))
                .thenReturn(true);

        boolean actual = pollVoteHandler.removeVote(pollDataMock, reactionEventWithTextMock);

        verify(memberServiceMock).getMemberData(1L);
        verify(pollAnswerServiceMock).removeVote(pollDataMock, memberDataMock, "emoji");
        assertTrue(actual);
    }

    @Test
    void removeVoteWhenVoteHasDeadlineAndIsOverSavesNothingAndClosesVote() {
        when(pollDataMock.isClosed()).thenReturn(false);
        when(pollDataMock.getDeadLine()).thenReturn(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(5));

        boolean actual = pollVoteHandler.removeVote(pollDataMock, reactionEventWithTextMock);

        verify(closePollCommandMock).closePoll(pollDataMock);
        verifyNoInteractions(memberServiceMock);
        verifyNoInteractions(pollAnswerServiceMock);
        assertFalse(actual);
    }
}