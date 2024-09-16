package com.therealtehu.discordbot.TehuBot.model.action.event.poll;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.MemberRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.ClosePollCommand;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventName;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.service.poll.MessageReactionEventWithText;
import com.therealtehu.discordbot.TehuBot.service.poll.PollAnswerService;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PollVoteEvent extends EventHandler {
    private static final Pattern POLL_ID_PATTERN = Pattern.compile("(?i)poll id:__\\s*([0-9]+-[0-9]+)");
    private final PollRepository pollRepository;
    private final PollAnswerService pollAnswerService;
    private final ClosePollCommand closePollCommand;
    private final MemberRepository memberRepository;

    @Autowired
    protected PollVoteEvent(MessageSender messageSender, PollRepository pollRepository,
                            PollAnswerService pollAnswerService, ClosePollCommand closePollCommand,
                            MemberRepository memberRepository) {
        super(EventName.POLL_VOTE.getEventName(), messageSender);
        this.pollRepository = pollRepository;
        this.pollAnswerService = pollAnswerService;
        this.closePollCommand = closePollCommand;
        this.memberRepository = memberRepository;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof MessageReactionEventWithText reactionAddEvent) {
            Matcher matcher = POLL_ID_PATTERN.matcher(reactionAddEvent.getImmediateMessage());
            if (matcher.find()) {
                String pollId = matcher.group(1);
                Optional<PollData> optionalPollData = pollRepository.findByPublicId(pollId);
                optionalPollData.ifPresentOrElse(pollData -> handleVote(pollData, reactionAddEvent),
                        () -> messageSender.sendMessage(reactionAddEvent.getChannel().asTextChannel(),
                        "ERROR: Could not find poll"));
            } else {
                messageSender.sendMessage(reactionAddEvent.getChannel().asTextChannel(),
                        "ERROR: Could not find poll id");
            }
        }

    }
    @Transactional
    public boolean handleVote(PollData pollData, MessageReactionEventWithText reactionAddEvent) {
        if(pollData.isAnonymous()) {
            reactionAddEvent.getReaction().clearReactions().queue();
        }

        if(pollData.isClosed()) {
            return false;
        }

        if(pollData.getDeadLine() != null && pollData.getDeadLine().isBefore(OffsetDateTime.now(ZoneOffset.UTC))) {
            closePollCommand.closePoll(pollData);
            return false;
        }

        Optional<MemberData> optionalMember = memberRepository.findByUserId(reactionAddEvent.getUserIdLong());
        if(optionalMember.isEmpty()) {
            messageSender.sendMessage(reactionAddEvent.getChannel().asTextChannel(),
                    "ERROR: Could not find user");
            return false;
        }

        MemberData memberData = optionalMember.get();

        if(pollData.isAnonymous() && isDoubleVote(pollData, memberData, reactionAddEvent.getEmoji().getAsReactionCode())) {
            pollAnswerService.removeVote(pollData, memberData, reactionAddEvent.getEmoji().getAsReactionCode());
            return true;
        }

        if(!hasPermissionToVote(reactionAddEvent, pollData) || !hasRemainingVotes(memberData, pollData)) {
            reactionAddEvent.getReaction().removeReaction(reactionAddEvent.getUser()).queue();
            return false;
        }

        Optional<PollAnswerData> optionalPollAnswerData = pollAnswerService
                .getPollAnswerData(pollData, reactionAddEvent.getEmoji().getAsReactionCode());

        if(optionalPollAnswerData.isPresent()) {
            PollAnswerData answerData = optionalPollAnswerData.get();

            answerData.addMember(memberData);
            pollAnswerService.saveAnswer(answerData);

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
