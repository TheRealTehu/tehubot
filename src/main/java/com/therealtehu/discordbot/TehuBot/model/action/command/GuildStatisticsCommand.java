package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildStatisticsData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildStatisticsRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GuildStatisticsCommand extends CommandWithFunctionality{
    private static final String COMMAND_NAME = "guildstatistics";
    private static final String COMMAND_DESCRIPTION = "Get information about bot use on server.";
    private final GuildStatisticsRepository guildStatisticsRepository;

    @Autowired
    public GuildStatisticsCommand(MessageSender messageSender, GuildStatisticsRepository guildStatisticsRepository) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, messageSender);
        this.guildStatisticsRepository = guildStatisticsRepository;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        if(event.getMember().hasPermission(Permission.MESSAGE_SEND)) {
            Optional<GuildStatisticsData> guildStatisticsData = guildStatisticsRepository
                    .findByGuildId(event.getGuild().getIdLong());
            if (guildStatisticsData.isPresent()) {
                messageSender.reply(event, guildStatisticsData.get().toString());
            } else {
                messageSender.reply(event, "DATABASE ERROR: Guild not found!");
            }
        }
    }
}
