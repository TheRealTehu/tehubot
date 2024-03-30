package com.therealtehu.discordbot.TehuBot.listeners.event;

import com.therealtehu.discordbot.TehuBot.listeners.event.guild.ServerJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventListener extends ListenerAdapter {
    private final ServerJoinEvent serverJoinEvent;
    @Autowired
    public EventListener(ServerJoinEvent serverJoinEvent) {
        this.serverJoinEvent = serverJoinEvent;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        serverJoinEvent.setup(event);
    }
}
