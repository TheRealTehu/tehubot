package com.therealtehu.discordbot.TehuBot.model.event.guild;

import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.EventName;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class ServerNewMemberEvent extends EventHandler {

    public ServerNewMemberEvent() {
        super(EventName.SERVER_NEW_MEMBER.getEventName());
    }

    @Override
    public void handle(Event event) {
        if(event instanceof GuildMemberJoinEvent guildMemberJoinEvent) {
            guildMemberJoinEvent.getGuild().getDefaultChannel().asTextChannel().sendMessage("Hi").queue();
        }
    }
}
