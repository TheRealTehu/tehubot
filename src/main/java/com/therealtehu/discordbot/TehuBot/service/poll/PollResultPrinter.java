package com.therealtehu.discordbot.TehuBot.service.poll;

import com.therealtehu.discordbot.TehuBot.bot.GuildFinder;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.service.display.Display;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PollResultPrinter {
    private final Display display;
    private final GuildFinder guildFinder;
    private final StringBuilder stringBuilder;

    public PollResultPrinter(Display display, GuildFinder guildFinder) {
        this.display = display;
        stringBuilder = new StringBuilder();
        this.guildFinder = guildFinder; //TODO: Find guild by id and send result
    }

    public void printResult(PollData pollData) {
        int allVotes = pollData.getNumberOfVotes();
        stringBuilder.append("Poll: ").append(allVotes).append(" has closed with ").append("*")
                .append(pollData.getNumberOfVotes()).append("*").append(" votes!").append("\n")
                .append("__").append(pollData.getPollDescription()).append("__").append("\n");
        for (Map.Entry<PollAnswerData, Integer> entry : pollData.getAnswersInOrder().entrySet()) {
            stringBuilder.append(entry.getKey().getAnswerText()).append(" ").append(entry.getValue()).append(" (")
                    .append(calculatePercentage(allVotes, entry.getValue())).append("%)");
        }

        pollData.getGuild().getBotChatChannelId();

        display.sendMessage(pollData);
    }

    private Double calculatePercentage(int maxVote, int numberOfVotes) {
        return ((double) maxVote / 100) * numberOfVotes;
    }
}
