package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoinFlipCommand extends CommandWithFunctionality{
    private static final String COMMAND_NAME = "coinflip";
    private static final String COMMAND_DESCRIPTION = "Flip a coin to help with decisions";

    private final RandomNumberGenerator randomNumberGenerator;
    @Autowired
    public CoinFlipCommand(MessageSender messageSender, RandomNumberGenerator randomNumberGenerator) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, messageSender);
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        int decision = randomNumberGenerator.getRandomNumber(100);
        String conclusion = event.getMember().getAsMention() + " has flipped a coin and ";
        if(decision < 49) {
            conclusion += "it was HEAD!";
        } else if (decision < 98) {
            conclusion += "it was TAIL!";
        } else if (decision == 98) {
            conclusion += "the coin STOOD ON IT'S SIDE!";
        } else {
            conclusion += "the coin bounced behind the couch, you can't find it!";
        }
        messageSender.replyToEvent(event, conclusion);
    }
}
