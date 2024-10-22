package com.therealtehu.discordbot.TehuBot.service.poll;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.ClosePollCommand;
import com.therealtehu.discordbot.TehuBot.service.MemberService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;

@Service
public class PollVoteHandler {
    private final PollAnswerService pollAnswerService;
    private final ClosePollCommand closePollCommand;
    private final MemberService memberService;
    private final MessageSender messageSender;

    @Autowired
    public PollVoteHandler(PollAnswerService pollAnswerService, ClosePollCommand closePollCommand,
                           MemberService memberService, MessageSender messageSender) {
        this.pollAnswerService = pollAnswerService;
        this.closePollCommand = closePollCommand;
        this.memberService = memberService;
        this.messageSender = messageSender;
    }

    @Transactional
    public boolean handleVote(PollData pollData, MessageReactionEventWithText reactionAddEvent) {
        if(pollData.isAnonymous()) {
            reactionAddEvent.getReaction().clearReactions().queue();
        }

        if (isVoteOver(pollData)) return false;

        MemberData memberData;
        try {
            memberData = memberService.getMemberData(reactionAddEvent);
        } catch (NoSuchElementException e) {
            messageSender.sendMessage(reactionAddEvent.getChannel().asTextChannel(),
                    "ERROR: Could not find user");
            return false;
        }

        if(pollData.isAnonymous() && isDoubleVote(pollData, memberData, reactionAddEvent.getEmoji().getAsReactionCode())) {
            pollAnswerService.removeVote(pollData, memberData, reactionAddEvent.getEmoji().getAsReactionCode());
            return true;
        }

        if(!hasPermissionToVote(reactionAddEvent, pollData) || !hasRemainingVotes(memberData, pollData)) {
            reactionAddEvent.getReaction().removeReaction(reactionAddEvent.getUser()).queue();
            return false;
        }

        return pollAnswerService.addVote(pollData, reactionAddEvent.getEmoji().getAsReactionCode(), memberData);
    }

    @Transactional
    public boolean removeVote(PollData pollData, MessageReactionEventWithText reactionRemoveEvent) {
        if (isVoteOver(pollData)) return false;

        MemberData memberData;

        try {
            memberData = memberService.getMemberData(reactionRemoveEvent);
        } catch (NoSuchElementException e) {
            messageSender.sendMessage(reactionRemoveEvent.getChannel().asTextChannel(),
                    "ERROR: Could not find user");
            return false;
        }

        return pollAnswerService.removeVote(pollData, memberData, reactionRemoveEvent.getEmoji().getAsReactionCode());
    }

    private boolean isVoteOver(PollData pollData) {
        if (pollData.isClosed()) {
            return true;
        }

        if (pollData.getDeadLine() != null && pollData.getDeadLine().isBefore(OffsetDateTime.now(ZoneOffset.UTC))) {
            closePollCommand.closePoll(pollData);
            return true;
        }
        return false;
    }

    private boolean hasRemainingVotes(MemberData memberData, PollData pollData) {
        return pollAnswerService.countVotes(pollData, memberData) < pollData.getNumberOfVotesPerMember();
    }

    private boolean hasPermissionToVote(MessageReactionEventWithText reactionAddEvent, PollData pollData) {
        if(pollData.getMinimumRole() == null) {
            return true;
        }

        try {
            Role minimumRole = findGuildRoleByName(reactionAddEvent, pollData);

            if(minimumRole.getPosition() == -1) {
                return true;
            }

            return reactionAddEvent.getMember().getRoles()
                    .stream()
                    .anyMatch(role -> role.getPosition() >= minimumRole.getPosition());

        } catch (NoSuchElementException e) {
            messageSender.sendMessage(reactionAddEvent.getChannel().asTextChannel(),
                    "ERROR: Could not find role");
        }
        return false;
    }

    private boolean isDoubleVote(PollData pollData, MemberData memberData, String answerEmoji) {
        return pollAnswerService.voteExistsForMember(memberData, pollData, answerEmoji);
    }

    private Role findGuildRoleByName(MessageReactionEventWithText reactionAddEvent, PollData pollData) throws NoSuchElementException {
        return reactionAddEvent.getGuild()
                .getRolesByName(pollData.getMinimumRole(), true)
                .stream().findFirst()
                .orElseThrow();
    }
}
