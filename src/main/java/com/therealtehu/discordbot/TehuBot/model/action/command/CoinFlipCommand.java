package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.CoinFlipData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.CoinFlipRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class CoinFlipCommand extends CommandWithFunctionality {
    private static final String COMMAND_NAME = "coinflip";
    private static final String COMMAND_DESCRIPTION = "Flip a coin to help with decisions";
    private final RandomNumberGenerator randomNumberGenerator;
    private final CoinFlipRepository coinFlipRepository;
    private final GuildRepository guildRepository;

    @Autowired
    public CoinFlipCommand(MessageSender messageSender, RandomNumberGenerator randomNumberGenerator,
                           CoinFlipRepository coinFlipRepository, GuildRepository guildRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, messageSender);
        this.randomNumberGenerator = randomNumberGenerator;
        this.coinFlipRepository = coinFlipRepository;
        this.guildRepository = guildRepository;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        int decision = randomNumberGenerator.getRandomNumber(100);
        String message = event.getMember().getAsMention() + " has flipped a coin and ";
        String conclusion = "";
        String dbData = "";
        if (decision < 49) {
            conclusion += "it was HEAD!";
            dbData = "Head";
        } else if (decision < 98) {
            conclusion += "it was TAIL!";
            dbData = "Tail";
        } else if (decision == 98) {
            conclusion += "the coin STOOD ON IT'S SIDE!";
            dbData = "Side";
        } else {
            conclusion += "the coin bounced behind the couch, you can't find it!";
            dbData = "Lost";
        }
        message += conclusion;
        try {
            saveToDatabase(event, dbData);
            messageSender.replyToEvent(event, message);
        } catch (NoSuchElementException e) {
            messageSender.replyToEvent(event, e.getMessage());
        }
    }

    private void saveToDatabase(SlashCommandInteractionEvent event, String dbData) {
        CoinFlipData coinFlipData = new CoinFlipData();
        coinFlipData.setFlippedSide(dbData);
        GuildData guild = guildRepository.findByGuildId(event.getGuild().getIdLong());
        if (guild == null) {
            throw new NoSuchElementException("DATABASE ERROR: Guild not found!");
        }
        coinFlipData.setGuild(guild);
        coinFlipRepository.save(coinFlipData);
    }
}
