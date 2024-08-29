package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.CommandWithFunctionality;
import com.therealtehu.discordbot.TehuBot.model.action.command.OptionName;
import com.therealtehu.discordbot.TehuBot.model.action.event.poll.PollAnswerHandler;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import jakarta.transaction.Transactional;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Component
public class CreatePollCommand extends CommandWithFunctionality {
    private static final String COMMAND_NAME = "createpoll";
    private static final String COMMAND_DESCRIPTION = "Start a vote on the server";
    private final PollAnswerHandler pollAnswerHandler;
    private final PollRepository pollRepository;
    private final GuildRepository guildRepository;

    @Autowired
    public CreatePollCommand(MessageSender messageSender, PollAnswerHandler pollAnswerHandler,
                             PollRepository pollRepository, GuildRepository guildRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, PollUtil.getOptions(), messageSender);
        this.pollAnswerHandler = pollAnswerHandler;
        this.pollRepository = pollRepository;
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
            try {
                PollData pollData = createPollData(event, guildDataOptional.get());

                pollRepository.save(pollData);

                List<PollAnswerData> answerData = pollAnswerHandler.saveAnswers(event, pollData);

                pollData.setAnswers(answerData);

                String response = createFormattedPoll(pollData);

                messageSender.replyToEvent(event, response);
            } catch (DateTimeParseException e) {
                messageSender.replyToEvent(event, "OPTION ERROR: Invalid time limit format!");
            }
        }
    }

    private String createFormattedPoll(PollData pollData) {
        StringBuilder stringBuilder = new StringBuilder();
        String endDate = pollData.getDeadLine() == null ? "when manually closed" : formatDeadLine(pollData);
        String isAnon = pollData.isAnonymous() ? "Yes" : "No";
        String minRole = pollData.getMinimumRole() == null ? "Anyone" : pollData.getMinimumRole();
        stringBuilder
                .append("__poll id:__ ").append(pollData.getPublicId()).append("\n")
                .append("**Rules: **").append("\n")
                .append("Number of votes per user: ").append(pollData.getNumberOfVotesPerMember()).append("\n")
                .append("Minimum role to participate: ").append(minRole).append("\n")
                .append("Is vote anonymous: ").append(isAnon).append("\n")
                .append("Poll ends: ").append(endDate).append("\n").append("\n")
                .append("**__").append(pollData.getPollDescription()).append("__**").append("\n").append("\n");
        for (PollAnswerData answer : pollData.getAnswers()) {
            stringBuilder.append(answer.getAnswerEmoji()).append(" ").append(answer.getAnswerText()).append("\n");
        }

        return stringBuilder.toString();
    }

    @NotNull
    private static String formatDeadLine(PollData pollData) {
        return pollData.getDeadLine().format(PollUtil.getDateFormatter());
    }

    private PollData createPollData(SlashCommandInteractionEvent event, GuildData guildData) throws DateTimeParseException {
        Optional<OffsetDateTime> endTime = getTimeLimit(event);
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
        Long largestPollId = pollRepository.findLatestIdForGuild(guildId);
        largestPollId = largestPollId == null ? 1 : largestPollId + 1;

        return guildId + "-" + largestPollId;
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

    private Optional<OffsetDateTime> getTimeLimit(SlashCommandInteractionEvent event) throws DateTimeParseException {
        OptionMapping timeLimit = event.getOption(OptionName.POLL_TIME_LIMIT_OPTION.getOptionName());
        if (timeLimit == null) {
            return Optional.empty();
        }

        LocalDateTime dateTime = LocalDateTime.parse(timeLimit.getAsString(), PollUtil.getDateFormatter());
        ZoneOffset offset = event.getTimeCreated().getOffset();
        OffsetDateTime endTime = OffsetDateTime.of(dateTime, offset);

        return Optional.of(endTime);
    }
}
