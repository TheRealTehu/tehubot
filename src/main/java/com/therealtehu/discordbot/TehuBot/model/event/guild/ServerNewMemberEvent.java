package com.therealtehu.discordbot.TehuBot.model.event.guild;

import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.EventName;
import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.springframework.stereotype.Service;

@Service
public class ServerNewMemberEvent extends EventHandler {
    private final TenorGifService tenorGifService;

    public ServerNewMemberEvent(TenorGifService tenorGifService) {
        super(EventName.SERVER_NEW_MEMBER.getEventName());
        this.tenorGifService = tenorGifService;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GuildMemberJoinEvent guildMemberJoinEvent) {
            String member = guildMemberJoinEvent.getMember().getAsMention();
            String message = member + " just joined the channel! Say hi everyone!";
            MessageEmbed gif = tenorGifService.getGifAsEmbed("Welcome");
            guildMemberJoinEvent
                    .getGuild()
                    .getDefaultChannel()
                    .asTextChannel()
                    .sendMessage(message)
                    .addEmbeds(gif)
                    .queue();
        }
    }
}
