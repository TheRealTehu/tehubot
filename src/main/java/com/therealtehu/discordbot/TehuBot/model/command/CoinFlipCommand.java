package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;

public class CoinFlipCommand extends CommandWithFunctionality{
    private static final String COMMAND_NAME = "coinflip";
    private static final String COMMAND_DESCRIPTION = "Flip a coin to help with decisions";
    public CoinFlipCommand(MessageSender messageSender) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, messageSender);
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        int decision = (int)(Math.random() * 100);
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
