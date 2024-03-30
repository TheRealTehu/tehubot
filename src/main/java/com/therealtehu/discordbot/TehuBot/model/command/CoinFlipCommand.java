package com.therealtehu.discordbot.TehuBot.model.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class CoinFlipCommand extends CommandWithFunctionality{
    private static final CommandDataImpl commandData =
            (CommandDataImpl) Commands.slash("coinflip", "Flip a coin to help with decisions");

    public CoinFlipCommand() {
        super(commandData);
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
        event.reply(conclusion).queue();
    }
}
