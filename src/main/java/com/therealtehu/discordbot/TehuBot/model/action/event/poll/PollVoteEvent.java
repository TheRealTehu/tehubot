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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;

@Component
public class PollVoteEvent extends EventHandler { //TODO: Check unit tests
    private final PollRepository pollRepository;
    private final PollVoteHandler pollVoteHandler;

    @Autowired
    protected PollVoteEvent(MessageSender messageSender, PollRepository pollRepository, PollVoteHandler pollVoteHandler) {
        super(EventName.POLL_VOTE.getEventName(), messageSender);
        this.pollRepository = pollRepository;
        this.pollVoteHandler = pollVoteHandler;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof MessageReactionEventWithText reactionAddEvent) {
            Matcher matcher = PollUtil.POLL_ID_PATTERN.matcher(reactionAddEvent.getImmediateMessage());
            if (matcher.find()) {
                String pollId = matcher.group(1);
                Optional<PollData> optionalPollData = pollRepository.findByPublicId(pollId);
                optionalPollData.ifPresentOrElse(pollData -> pollVoteHandler.handleVote(pollData, reactionAddEvent),
                        () -> messageSender.sendMessage(reactionAddEvent.getChannel().asTextChannel(),
                        "ERROR: Could not find poll"));
            } else {
                messageSender.sendMessage(reactionAddEvent.getChannel().asTextChannel(),
                        "ERROR: Could not find poll id");
            }
        }
    }

}
