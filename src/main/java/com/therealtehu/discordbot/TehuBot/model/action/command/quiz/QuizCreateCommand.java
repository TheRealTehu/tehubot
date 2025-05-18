package com.therealtehu.discordbot.TehuBot.model.action.command.quiz;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.model.quiz.QuizData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.quiz.QuizRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.CommandWithFunctionality;
import com.therealtehu.discordbot.TehuBot.model.action.command.OptionName;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QuizCreateCommand extends CommandWithFunctionality {
    private static final String COMMAND_NAME = "quizcreate";
    private static final String COMMAND_DESCRIPTION = "Create a new, empty quiz on the server";
    private static final int MAX_TITLE_LENGTH = 100;
    private static final OptionData TITLE_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.QUIZ_TITLE_OPTION.getOptionName(),
            "The title of the quiz (max length:" + MAX_TITLE_LENGTH + ")",
            true).setMaxLength(MAX_TITLE_LENGTH);
    private final GuildRepository guildRepository;
    private final QuizRepository quizRepository;

    @Autowired
    public QuizCreateCommand(MessageSender messageSender, GuildRepository guildRepository, QuizRepository quizRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, List.of(TITLE_OPTION), messageSender);
        this.guildRepository = guildRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        if (event.getMember().hasPermission(Permission.USE_EMBEDDED_ACTIVITIES)) {
            Long guildId = event.getGuild().getIdLong();

            Optional<GuildData> guildDataOptional = guildRepository.findById(guildId);

            if (guildDataOptional.isEmpty()) {
                messageSender.reply(event, "DATABASE ERROR: Guild not found!");
            } else {
                QuizData quizData = new QuizData();
                quizData.setPublicId(getPublicId(guildId));
                quizData.setGuild(guildDataOptional.get());
                quizData.setQuizTitle(event.getOption(OptionName.QUIZ_TITLE_OPTION.getOptionName()).getAsString());

                quizRepository.save(quizData);

                messageSender.reply(event, "Quiz with Title: " + quizData.getQuizTitle() + " and publicId: " + quizData.getPublicId() + " has been created!");
            }
        }
    }

    private String getPublicId(Long guildId) {
        Long largestPollId = quizRepository.findLatestIdForGuild(guildId);
        largestPollId = largestPollId == null ? 1 : largestPollId + 1;

        return guildId + "-" + largestPollId;
    }
}
