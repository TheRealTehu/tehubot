package com.therealtehu.discordbot.TehuBot.model.action.event.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.ClosePollCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class PollCloseScheduler {
    private static final Logger log = LoggerFactory.getLogger(PollCloseScheduler.class);
    private final PollRepository pollRepository;
    private final ClosePollCommand closePollCommand;

    public PollCloseScheduler(PollRepository pollRepository, ClosePollCommand closePollCommand) {
        this.pollRepository = pollRepository;
        this.closePollCommand = closePollCommand;
    }

    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() {
        log.info("The time is now {}", OffsetDateTime.now(ZoneOffset.UTC));
    }

    @Scheduled(fixedRate = 60000)
    public void closeExpiredPolls() {
        List<PollData> openPolls = pollRepository
                .findByIsClosedFalseAndDeadLineIsNotNullAndDeadLineBefore(OffsetDateTime.now(ZoneOffset.UTC));
        for (PollData polldata : openPolls) {
            closePollCommand.closePoll(polldata);
        }
    }
}
