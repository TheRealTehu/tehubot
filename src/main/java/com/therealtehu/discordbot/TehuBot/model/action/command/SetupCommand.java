package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

@Service
public class SetupCommand extends CommandWithFunctionality{

    private static final String COMMAND_NAME = "setup";
    private static final String COMMAND_DESCRIPTION = "Quick setup for the bot.";
    private final GuildRepository guildRepository;

    public SetupCommand(MessageSender messageSender, GuildRepository guildRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, messageSender);
        this.guildRepository = guildRepository;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        GuildData guildData = new GuildData();

        guildData.setId(event.getGuild().getIdLong());
        guildData.setBotChatChannelId(event.getGuild().getDefaultChannel().getIdLong());

        guildRepository.save(guildData);

        event.reply("Guild saved to DB").queue();
    }
}
