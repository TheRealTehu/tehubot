package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.DiceRollData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.DiceRollRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class DiceRollCommand extends CommandWithFunctionality{
    private static final int MIN_DICE_SIDES = 3;
    private static final int MAX_DICE_SIDES = 100;
    private static final int DEFAULT_DICE_SIDES = 6;
    private static final OptionData SIDES_OPTION = new OptionData(
            OptionType.INTEGER,
            OptionName.DICE_ROLL_SIDES_OPTION.getOptionName(),
            "The number of sides of the die. Default: "+ DEFAULT_DICE_SIDES + " (Should be between "
                    + MIN_DICE_SIDES + " and " + MAX_DICE_SIDES + ")",
            false).setMinValue(MIN_DICE_SIDES).setMaxValue(MAX_DICE_SIDES);
    private static final String COMMAND_NAME = "diceroll";
    private static final String COMMAND_DESCRIPTION = "Roll an N-sided dice ("
            + MIN_DICE_SIDES + " <= N <= "+ MAX_DICE_SIDES + ")";
    private final RandomNumberGenerator randomNumberGenerator;
    private final DiceRollRepository diceRollRepository;
    private final GuildRepository guildRepository;
    @Autowired
    public DiceRollCommand(MessageSender messageSender, RandomNumberGenerator randomNumberGenerator,
                           DiceRollRepository diceRollRepository, GuildRepository guildRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, List.of(SIDES_OPTION), messageSender);
        this.randomNumberGenerator = randomNumberGenerator;
        this.diceRollRepository = diceRollRepository;
        this.guildRepository = guildRepository;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        if(event.getMember().hasPermission(Permission.MESSAGE_SEND)) {
            OptionMapping optionData = event.getOption(OptionName.DICE_ROLL_SIDES_OPTION.getOptionName());
            int numberOfSides = DEFAULT_DICE_SIDES;
            if (optionData != null) {
                numberOfSides = optionData.getAsInt();
            }
            int rolledNumber = randomNumberGenerator.getRandomNumber(1, numberOfSides);
            String message;

            try {
                saveToDatabase(numberOfSides, rolledNumber, event);
                message = event.getMember().getAsMention() + " rolled a " + numberOfSides
                        + " sided die and the result is: " + rolledNumber + "!";
            } catch (NoSuchElementException e) {
                message = e.getMessage();
            }

            messageSender.reply(event, message);
        }
    }

    private void saveToDatabase(int numberOfSides, int rolledNumber, SlashCommandInteractionEvent event) {
        Optional<GuildData> guild = guildRepository.findById(event.getGuild().getIdLong());
        if(guild.isEmpty()) {
            throw new NoSuchElementException("DATABASE ERROR: Guild not found!");
        }
        DiceRollData diceRollData = new DiceRollData();
        diceRollData.setGuild(guild.get());
        diceRollData.setNumberOfSides(numberOfSides);
        diceRollData.setRolledNumber(rolledNumber);

        diceRollRepository.save(diceRollData);
    }
}
