package com.therealtehu.discordbot.TehuBot.model.action.event.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventName;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.service.poll.MessageReactionEventWithText;
import com.therealtehu.discordbot.TehuBot.service.poll.PollUtil;
import com.therealtehu.discordbot.TehuBot.service.poll.PollVoteHandler;
import net.dv8tion.jda.api.events.Event;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;

@Component
public class PollRemoveVoteEvent extends EventHandler {
    private final PollRepository pollRepository;
    private final PollVoteHandler pollVoteHandler;

    protected PollRemoveVoteEvent(MessageSender messageSender, PollRepository pollRepository,
                                  PollVoteHandler pollVoteHandler) {
        super(EventName.POLL_REMOVE_VOTE.getEventName(), messageSender);
        this.pollRepository = pollRepository;
        this.pollVoteHandler = pollVoteHandler;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof MessageReactionEventWithText reactionRemoveEvent) {
            Matcher matcher = PollUtil.POLL_ID_PATTERN.matcher(reactionRemoveEvent.getImmediateMessage());
            if (matcher.find()) {
                String pollId = matcher.group(1);
                Optional<PollData> optionalPollData = pollRepository.findByPublicId(pollId);
                optionalPollData.ifPresentOrElse(pollData -> pollVoteHandler.removeVote(pollData, reactionRemoveEvent),
                        () -> messageSender.sendMessage(reactionRemoveEvent.getChannel().asTextChannel(),
                                "ERROR: Could not find poll"));
            } else {
                messageSender.sendMessage(reactionRemoveEvent.getChannel().asTextChannel(),
                        "ERROR: Could not find poll id");
            }
        }
    }
}
