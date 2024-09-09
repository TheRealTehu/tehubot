package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.CommandWithFunctionality;
import com.therealtehu.discordbot.TehuBot.model.action.command.OptionName;
import com.therealtehu.discordbot.TehuBot.model.action.event.poll.PollResultPrinter;
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
public class ClosePollCommand extends CommandWithFunctionality {
    private static final OptionData POLL_ID_OPTION = new OptionData(
            OptionType.STRING,
            OptionName.POLL_ID_OPTION.getOptionName(),
            "The id of the poll you want to close.",
            true);

    private static final String COMMAND_NAME = "closepoll";
    private static final String COMMAND_DESCRIPTION = "Close an open poll (only users with MANAGE EVENTS permission can close polls)";
    private final PollRepository pollRepository;
    private final PollResultPrinter pollResultPrinter;

    @Autowired
    public ClosePollCommand(PollRepository pollRepository, MessageSender messageSender, PollResultPrinter pollResultPrinter) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, List.of(POLL_ID_OPTION), messageSender);
        this.pollRepository = pollRepository;
        this.pollResultPrinter = pollResultPrinter; //TODO: Use PollResultPrinter and rewrite tests
    }
    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        if(event.getMember().getPermissions().contains(Permission.MANAGE_EVENTS)) {
            String pollId = event.getOption(OptionName.POLL_ID_OPTION.getOptionName()).getAsString();
            Optional<PollData> optionalPollData = pollRepository.findByPublicId(pollId);
            optionalPollData.ifPresentOrElse(this::closePoll,
                    () -> messageSender.replyToEvent(event, "ERROR: Could not find poll by id"));
        }
    }
    public void closePoll(PollData pollData) {
        if(!pollData.isClosed()) {
            pollData.setClosed(true);
            pollRepository.save(pollData);
            pollResultPrinter.printResult(pollData);
        }
    }
}
