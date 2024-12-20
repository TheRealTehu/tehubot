package com.therealtehu.discordbot.TehuBot.model.action.event.guild.server_join;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventName;
import com.therealtehu.discordbot.TehuBot.service.MemberService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerJoinEvent extends EventHandler {
    private static final String GREETING_TEXT = "Hey! I'm TehuBot! I'm new on this server, a server admin please go through my initial setup!\n Where should I post?\n";
    private final MessageCreateBuilder messageCreateBuilder;
    protected static final String DROP_DOWN_EVENT_ID = "choose-bot-channel";
    private final MemberService memberService;
    private final GuildRepository guildRepository;

    private static final EntitySelectMenu channelDropDown = EntitySelectMenu
            .create(DROP_DOWN_EVENT_ID, EntitySelectMenu.SelectTarget.CHANNEL)
            .setChannelTypes(ChannelType.TEXT)
            .setRequiredRange(1, 1)
            .build();

    @Autowired
    public ServerJoinEvent(MessageSender messageSender, MessageCreateBuilder messageCreateBuilder,
                           MemberService memberService, GuildRepository guildRepository) {
        super(EventName.SERVER_JOIN.getEventName(), messageSender);
        this.messageCreateBuilder = messageCreateBuilder;
        this.memberService = memberService;
        this.guildRepository = guildRepository;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GuildJoinEvent guildJoinEvent) {
            TextChannel channel = guildJoinEvent.getGuild().getDefaultChannel().asTextChannel();
            if (guildRepository.findById(guildJoinEvent.getGuild().getIdLong()).isPresent()) {
                messageSender.sendMessage(channel, "Welcome back! Previous setup loaded!");
            } else {
                GuildData guildData = new GuildData();
                guildData.setId(guildJoinEvent.getGuild().getIdLong());
                guildRepository.save(guildData);

                MessageCreateData messageCreateData = messageCreateBuilder
                        .addContent(GREETING_TEXT)
                        .addActionRow(channelDropDown)
                        .build();
                messageSender.sendMessage(channel, messageCreateData);
            }
            memberService.addMembersFromGuild(guildJoinEvent.getGuild());
        }
    }
}
