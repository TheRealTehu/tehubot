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
    private final StringBuilder stringBuilder;

    @Autowired
    public PollResultPrinter(MessageSender messageSender, GuildFinder guildFinder) {
        this.messageSender = messageSender;
        this.guildFinder = guildFinder;
        stringBuilder = new StringBuilder();
    }

    public void printResult(PollData pollData) {
        buildResultText(pollData);
        sendResult(pollData);
    }

    private void sendResult(PollData pollData) {
        Optional<Guild> optionalGuild = guildFinder.findGuildBy(pollData.getGuild().getId());
        optionalGuild.ifPresent(guild -> {
            TextChannel botTextChannel = guild.getTextChannelById(pollData.getGuild().getBotChatChannelId());
            messageSender.sendMessage(botTextChannel, stringBuilder.toString());
        });
    }

    private void buildResultText(PollData pollData) {
        buildResultHeader(pollData);
        buildResultAnswers(pollData);
    }
    private void buildResultHeader(PollData pollData) {
        stringBuilder.append("Poll: ").append(pollData.getPublicId()).append(" has closed with ").append("*")
                .append(pollData.getNumberOfVotes()).append("*").append(" votes!").append("\n")
                .append("__").append(pollData.getPollDescription()).append("__").append("\n");
    }

    private void buildResultAnswers(PollData pollData) {
        for (PollAnswerData answerData : pollData.getAnswersInOrder()) {
            stringBuilder.append(answerData.getAnswerText()).append(": ").append(answerData.getNumberOfVotes()).append(" (")
                    .append(calculatePercentage(pollData.getNumberOfVotes(), answerData.getNumberOfVotes()))
                    .append("%)").append("\n");
        }
    }

    private Double calculatePercentage(int maxVote, int numberOfVotes) {
        return ((double) numberOfVotes / maxVote) * 100;
    }
}
