package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class DiceRollCommand extends CommandWithFunctionality{
    private static final int MIN_DICE_SIDES = 3;
    private static final int MAX_DICE_SIDES = 100;

    private static final int DEFAULT_DICE_SIDES = 6;
    private static final OptionData SIDES_OPTION = new OptionData(
            OptionType.INTEGER,
            "sides",
            "The number of sides of the die. Default: "+ DEFAULT_DICE_SIDES + " (Should be between "
                    + MIN_DICE_SIDES + " and " + MAX_DICE_SIDES + " 100)",
            false).setMinValue(MIN_DICE_SIDES).setMaxValue(MAX_DICE_SIDES);
    private static final String COMMAND_NAME = "diceroll";
    private static final String COMMAND_DESCRIPTION = "Roll an N-sided dice ("
            + MIN_DICE_SIDES + " <= N <= "+ MAX_DICE_SIDES + ")";
    @Autowired
    public DiceRollCommand(MessageSender messageSender) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, List.of(SIDES_OPTION), messageSender);
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        OptionMapping optionData = event.getOption("sides");
        int numberOfSides = DEFAULT_DICE_SIDES;
        if(optionData != null) {
            numberOfSides = optionData.getAsInt();
        }
        int rolledNumber = (int) (Math.random() * numberOfSides) + 1;

        String message = event.getMember().getAsMention() + " rolled a " + numberOfSides
                + " sided die and the result is: " + rolledNumber + "!";
        messageSender.replyToEvent(event, message);
    }
}
