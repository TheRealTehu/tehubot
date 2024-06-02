package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.model.action.command.CommandWithFunctionality;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//TODO: ASK if option names should be in enum
@Component
public class PollCommand extends CommandWithFunctionality {
    private static final String COMMAND_NAME = "poll";
    private static final String COMMAND_DESCRIPTION = "Start a vote on the server";
    private final RandomNumberGenerator randomNumberGenerator;


    @Autowired
    public PollCommand(MessageSender messageSender, RandomNumberGenerator randomNumberGenerator) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, PollUtil.getOptions(), messageSender);
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        Optional<LocalDateTime> endTime = getTimeLimit(event);
        int numberOfVotes = getNumberOfVotes(event);
        Optional<Role> minimumRole = getMinimumROle(event);
        boolean isAnonymous = isAnonymous(event);
        List<String> answers = getAnswers(event);

        messageSender.replyToEvent(event, answers.stream().reduce("", (substring, element) -> substring + " " + element));

    }

    private List<String> getAnswers(SlashCommandInteractionEvent event) {
        List<String> answers = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            OptionMapping answer = event.getOption("answer" + i);
            if(answer != null) {
                answers.add(answer.getAsString());
            }
        }
        List<Emoji> emojis = getEmojisToUse(event.getGuild().getEmojis(), answers.size());
        for (int i = 0; i < answers.size(); i++) {
            answers.set(i, emojis.get(i) + " " + answers.get(i));
        }
        return answers;
    }

    private List<Emoji> getEmojisToUse(List<RichCustomEmoji> guildEmojis, int size) {
        if(size < guildEmojis.size()) {
            return guildEmojis.stream().limit(size).map(emoji -> (Emoji) emoji).toList();
        } else {
            List<Emoji> emojisToUse = new ArrayList<>(guildEmojis);
            List<String> unicodeEmojis = PollUtil.getEmojis();
            while(emojisToUse.size() < size) {
                int pick = randomNumberGenerator.getRandomNumber(unicodeEmojis.size());
                Emoji unicodeEmoji = Emoji.fromUnicode(unicodeEmojis.get(pick));
                if(!emojisToUse.contains(unicodeEmoji)) {
                    emojisToUse.add(unicodeEmoji);
                }
            }
            return emojisToUse;
        }
    }

    private boolean isAnonymous(SlashCommandInteractionEvent event) {
        OptionMapping anonymousvote = event.getOption("anonymousvote");
        if (anonymousvote == null) {
            return false;
        }
        return anonymousvote.getAsBoolean();
    }

    private Optional<Role> getMinimumROle(SlashCommandInteractionEvent event) {
        OptionMapping minimumRole = event.getOption("minrole");
        if (minimumRole == null) {
            return Optional.empty();
        }
        return Optional.of(minimumRole.getAsRole());
    }

    private int getNumberOfVotes(SlashCommandInteractionEvent event) {
        OptionMapping numberOfVotes = event.getOption("numberofvotes");
        if (numberOfVotes == null) {
            return PollUtil.getDefaultNumberOfVotes();
        }
        return numberOfVotes.getAsInt();
    }

    private Optional<LocalDateTime> getTimeLimit(SlashCommandInteractionEvent event) throws DateTimeException {
        OptionMapping timeLimit = event.getOption("timelimit");
        if (timeLimit == null) {
            return Optional.empty();
        }
        LocalDateTime endTime = null;
        try {
            endTime = LocalDateTime.parse(timeLimit.getAsString(), PollUtil.getDateFormatter());
        } catch (DateTimeException e) {
            messageSender.replyToEvent(event, "Invalid time limit format!");
        }
        return Optional.of(endTime);
    }
}
