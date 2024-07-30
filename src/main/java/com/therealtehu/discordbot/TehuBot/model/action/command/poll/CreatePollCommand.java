package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollAnswerRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.CommandWithFunctionality;
import com.therealtehu.discordbot.TehuBot.model.action.command.OptionName;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import jakarta.transaction.Transactional;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CreatePollCommand extends CommandWithFunctionality {
    private static final String COMMAND_NAME = "createpoll";
    private static final String COMMAND_DESCRIPTION = "Start a vote on the server";
    private final RandomNumberGenerator randomNumberGenerator;
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final GuildRepository guildRepository;

    @Autowired
    public CreatePollCommand(MessageSender messageSender, RandomNumberGenerator randomNumberGenerator,
                             PollRepository pollRepository, PollAnswerRepository pollAnswerRepository,
                             GuildRepository guildRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, PollUtil.getOptions(), messageSender);
        this.randomNumberGenerator = randomNumberGenerator;
        this.pollRepository = pollRepository;
        this.pollAnswerRepository = pollAnswerRepository;
        this.guildRepository = guildRepository;
    }

    @Override
    @Transactional
    public void executeCommand(SlashCommandInteractionEvent event) {
        Long guildId = event.getGuild().getIdLong();

        Optional<GuildData> guildDataOptional = guildRepository.findById(guildId);

        if (guildDataOptional.isEmpty()) {
            messageSender.replyToEvent(event, "DATABASE ERROR: Guild not found!");
        } else {
            PollData pollData = createPollData(event, guildDataOptional.get());

            pollRepository.save(pollData);

            List<PollAnswerData> answerData = saveAnswers(event, pollData);

            pollData.setAnswers(answerData);

            String response = createFormattedPoll(pollData);

            messageSender.replyToEvent(event, response);
        }
    }

    private String createFormattedPoll(PollData pollData) {
        StringBuilder stringBuilder = new StringBuilder();
        String endDate = pollData.getDeadLine() == null ? "when manually closed" : pollData.getDeadLine().toString();
        String isAnon = pollData.isAnonymous() ? "Yes" : "No";
        String minRole = pollData.getMinimumRole() == null ? "Anyone" : pollData.getMinimumRole();
        stringBuilder
                .append("__poll id:__ ").append(pollData.getPublicId()).append("\n")
                .append("**Rules: **").append("\n")
                .append("Number of votes per user: ").append(pollData.getNumberOfVotesPerMember()).append("\n")
                .append("Minimum role to participate: ").append(minRole).append("\n")
                .append("Is vote anonymous: ").append(isAnon).append("\n")
                .append("Poll ends: ").append(endDate).append("\n")
                .append("**__").append(pollData.getPollDescription()).append("__**").append("\n");
        for (PollAnswerData answer: pollData.getAnswers()) {
            stringBuilder.append(answer.getAnswerEmoji()).append(" ").append(answer.getAnswerText()).append("\n");
        }

        return stringBuilder.toString();
    }

    private PollData createPollData(SlashCommandInteractionEvent event, GuildData guildData) {
        Optional<LocalDateTime> endTime = getTimeLimit(event);
        int numberOfVotes = getNumberOfVotes(event);
        Optional<Role> minimumRole = getMinimumRole(event);
        boolean isAnonymous = isAnonymous(event);
        String description = getPollDescription(event);

        String publicId = getPublicId(guildData.getId());

        PollData pollData = new PollData();
        pollData.setPublicId(publicId);
        pollData.setGuild(guildData);
        pollData.setPollDescription(description);
        pollData.setAnonymous(isAnonymous);
        endTime.ifPresent(pollData::setDeadLine);
        pollData.setNumberOfVotesPerMember(numberOfVotes);
        minimumRole.ifPresent(minRole -> pollData.setMinimumRole(minRole.getName()));

        return pollData;
    }

    private String getPublicId(Long guildId) {
        Long largestId = pollRepository.findLatestIdForGuild(guildId);
        largestId = largestId == null ? 1 : largestId + 1;

        return guildId + "-" + largestId;
    }

    private List<PollAnswerData> saveAnswers(SlashCommandInteractionEvent event, PollData pollData) {
        List<String> answerTexts = getAnswerTexts(event);
        List<Emoji> emojis = getEmojisToUse(event.getGuild().getEmojis(), answerTexts.size());

        List<PollAnswerData> answerData = new ArrayList<>();
        for (int i = 0; i < answerTexts.size(); i++) {
            PollAnswerData pollAnswerData = new PollAnswerData();
            pollAnswerData.setPollData(pollData);
            pollAnswerData.setAnswerText(answerTexts.get(i));
            pollAnswerData.setAnswerEmoji(emojis.get(i).getAsReactionCode());

            answerData.add(pollAnswerData);

        }
        pollAnswerRepository.saveAll(answerData);

        return answerData;
    }

    @NotNull
    private static List<String> getAnswerTexts(SlashCommandInteractionEvent event) {
        List<String> answers = new ArrayList<>();
        for (int i = 1; i <= PollUtil.getMaxNumberOfVoteOptions(); i++) {
            OptionMapping answer = event.getOption("answer" + i);
            if (answer != null) {
                answers.add(answer.getAsString());
            }
        }
        return answers;
    }

    private List<Emoji> getEmojisToUse(List<RichCustomEmoji> guildEmojis, int size) {
        if (size < guildEmojis.size()) {
            return guildEmojis.stream().limit(size).map(emoji -> (Emoji) emoji).toList();
        } else {
            List<Emoji> emojisToUse = new ArrayList<>(guildEmojis);
            List<String> unicodeEmojis = PollUtil.getEmojis();
            while (emojisToUse.size() < size) {
                int pick = randomNumberGenerator.getRandomNumber(unicodeEmojis.size());
                Emoji unicodeEmoji = Emoji.fromUnicode(unicodeEmojis.get(pick));
                if (!emojisToUse.contains(unicodeEmoji)) {
                    emojisToUse.add(unicodeEmoji);
                }
            }
            return emojisToUse;
        }
    }

    private String getPollDescription(SlashCommandInteractionEvent event) {
        OptionMapping description = event.getOption(OptionName.POLL_DESCRIPTION_OPTION.getOptionName());
        return description.getAsString();
    }

    private boolean isAnonymous(SlashCommandInteractionEvent event) {
        OptionMapping anonymousVote = event.getOption(OptionName.POLL_ANONYMOUS_OPTION.getOptionName());
        if (anonymousVote == null) {
            return false;
        }
        return anonymousVote.getAsBoolean();
    }

    private Optional<Role> getMinimumRole(SlashCommandInteractionEvent event) {
        OptionMapping minimumRole = event.getOption(OptionName.POLL_MINIMUM_ROLE_OPTION.getOptionName());
        if (minimumRole == null) {
            return Optional.empty();
        }
        return Optional.of(minimumRole.getAsRole());
    }

    private int getNumberOfVotes(SlashCommandInteractionEvent event) {
        OptionMapping numberOfVotes = event.getOption(OptionName.POLL_NUMBER_OF_VOTES_OPTION.getOptionName());
        if (numberOfVotes == null) {
            return PollUtil.getDefaultNumberOfVotes();
        }
        return numberOfVotes.getAsInt();
    }

    private Optional<LocalDateTime> getTimeLimit(SlashCommandInteractionEvent event) throws DateTimeException {
        OptionMapping timeLimit = event.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName());
        if (timeLimit == null) {
            return Optional.empty();
        }
        LocalDateTime endTime = null;
        try {
            endTime = LocalDateTime.parse(timeLimit.getAsString(), PollUtil.getDateFormatter());
        } catch (DateTimeException e) {
            messageSender.replyToEvent(event, "OPTION ERROR: Invalid time limit format!");
        }
        return Optional.of(endTime);
    }
}
