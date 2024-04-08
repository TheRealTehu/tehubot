package com.therealtehu.discordbot.TehuBot.model.event.guild;

import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.EventName;
import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerNewMemberEvent extends EventHandler {
    private final TenorGifService tenorGifService;

    @Autowired
    public ServerNewMemberEvent(TenorGifService tenorGifService, MessageSender messageSender) {
        super(EventName.SERVER_NEW_MEMBER.getEventName(), messageSender);
        this.tenorGifService = tenorGifService;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GuildMemberJoinEvent guildMemberJoinEvent) {
            String member = guildMemberJoinEvent.getMember().getAsMention();
            TextChannel channel = guildMemberJoinEvent.getGuild().getDefaultChannel().asTextChannel();
            String message = member + " just joined the channel! Say hi everyone!";
            MessageEmbed gif = tenorGifService.getGifAsEmbed("Welcome");

            messageSender.sendMessageWithMessageEmbed(channel, message, gif);
        }
    }
}
