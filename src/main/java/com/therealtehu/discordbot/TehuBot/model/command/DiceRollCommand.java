package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.List;

public class DiceRollCommand extends CommandWithFunctionality{
    private static final OptionData SIDES_OPTION = new OptionData(
            OptionType.INTEGER,
            "sides",
            "The number of sides of the die. Default: 6 (Should be between 3 and 100)",
            false).setMinValue(3).setMaxValue(100);
    private static final CommandDataImpl COMMAND_DATA =
            (CommandDataImpl) Commands.slash("diceroll", "Roll an N-sided dice (3 <= N <= 100)");

    public DiceRollCommand(MessageSender messageSender) {
        super(COMMAND_DATA, List.of(SIDES_OPTION), messageSender);
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        OptionMapping optionData = event.getOption("sides");
        int numberOfSides = 6;
        if(optionData != null) {
            numberOfSides = optionData.getAsInt();
        }
        int rolledNumber = (int) (Math.random() * numberOfSides) + 1;

        String message = event.getMember().getAsMention() + " rolled a " + numberOfSides
                + " sided die and the result is: " + rolledNumber + "!";
        messageSender.replyToEvent(event, message);
    }
}
