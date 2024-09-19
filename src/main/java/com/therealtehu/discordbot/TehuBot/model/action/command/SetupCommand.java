package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.MemberService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetupCommand extends CommandWithFunctionality {

    private static final String COMMAND_NAME = "setup";
    private static final String COMMAND_DESCRIPTION = "Quick setup for the bot.";
    private final GuildRepository guildRepository;
    private final MemberService memberService;

    @Autowired
    public SetupCommand(MessageSender messageSender, GuildRepository guildRepository, MemberService memberService) {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, messageSender);
        this.guildRepository = guildRepository;
        this.memberService = memberService;
    }

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        GuildData guildData = new GuildData();

        guildData.setId(guild.getIdLong());
        guildData.setBotChatChannelId(guild.getDefaultChannel().getIdLong());

        guildRepository.save(guildData);

        memberService.addMembersFromGuild(guild);

        event.reply("Guild saved to DB").queue();
    }
}
