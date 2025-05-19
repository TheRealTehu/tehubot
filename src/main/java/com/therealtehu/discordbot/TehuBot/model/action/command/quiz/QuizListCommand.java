package com.therealtehu.discordbot.TehuBot.model.action.command.quiz;

import com.therealtehu.discordbot.TehuBot.database.model.quiz.QuizData;
import com.therealtehu.discordbot.TehuBot.database.repository.quiz.QuizRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.CommandWithFunctionality;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizListCommand extends CommandWithFunctionality {
    private static final String COMMAND_NAME = "quizlist";
    private static final String COMMAND_DESCRIPTION = "Lists all quiz's on the server";
    private final QuizRepository quizRepository;

    @Autowired
    public QuizListCommand(MessageSender messageSender, QuizRepository quizRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, messageSender);
        this.quizRepository = quizRepository;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        StringBuilder stringBuilder = new StringBuilder();

        List<QuizData> quizes = quizRepository.findAllByGuildId(event.getGuild().getIdLong());
        if (quizes.isEmpty()) {
            messageSender.reply(event, "No quiz's found on the server!");
        } else {
            stringBuilder.append("List of quiz's on the server:\n");
            for (QuizData quiz : quizes) {
                stringBuilder.append(quiz.getListInformation()).append("\n");
            }
            messageSender.reply(event, stringBuilder.toString());
        }
    }
}
