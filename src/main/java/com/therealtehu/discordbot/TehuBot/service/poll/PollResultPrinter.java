package com.therealtehu.discordbot.TehuBot.service.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.service.GuildFinder;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PollResultPrinter {
    private final MessageSender messageSender;
    private final GuildFinder guildFinder;

    @Autowired
    public PollResultPrinter(MessageSender messageSender, GuildFinder guildFinder) {
        this.messageSender = messageSender;
        this.guildFinder = guildFinder;
    }

    public void printResult(PollData pollData) {
        StringBuilder stringBuilder = buildResultText(pollData);
        sendResult(pollData, stringBuilder);
    }

    private void sendResult(PollData pollData, StringBuilder stringBuilder) {
        Optional<Guild> optionalGuild = guildFinder.findGuildBy(pollData.getGuild().getId());
        optionalGuild.ifPresent(guild -> {
            TextChannel botTextChannel = guild.getTextChannelById(pollData.getGuild().getBotChatChannelId());
            messageSender.sendMessage(botTextChannel, stringBuilder.toString());
        });
    }

    private StringBuilder buildResultText(PollData pollData) {
        StringBuilder stringBuilder = buildResultHeader(pollData);
        stringBuilder.append(buildResultAnswers(pollData));
        return stringBuilder;
    }
    private StringBuilder buildResultHeader(PollData pollData) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Poll: ").append(pollData.getPublicId()).append(" has closed with ").append("*")
                .append(pollData.getNumberOfVotes()).append("*").append(" votes!").append("\n")
                .append("__").append(pollData.getPollDescription()).append("__").append("\n");
        return stringBuilder;
    }

    private StringBuilder buildResultAnswers(PollData pollData) {
        StringBuilder stringBuilder = new StringBuilder();
        for (PollAnswerData answerData : pollData.getAnswersInOrder()) {
            stringBuilder.append(answerData.getAnswerText()).append(": ").append(answerData.getNumberOfVotes()).append(" (")
                    .append(calculatePercentage(pollData.getNumberOfVotes(), answerData.getNumberOfVotes()))
                    .append("%)").append("\n");
        }
        return stringBuilder;
    }

    private Double calculatePercentage(int maxVote, int numberOfVotes) {
        if(maxVote == 0) {
            return 0.0;
        }
        return ((double) numberOfVotes / maxVote) * 100;
    }
}
