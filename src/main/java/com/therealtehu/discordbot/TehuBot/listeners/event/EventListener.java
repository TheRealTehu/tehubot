package com.therealtehu.discordbot.TehuBot.listeners.event;

import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.EventName;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventListener extends ListenerAdapter {
    private final List<EventHandler> eventHandlers;
    @Autowired
    public EventListener(List<EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Optional<EventHandler> serverJoinEventHandler = getEventHandler(EventName.SERVER_JOIN.getEventName());
        serverJoinEventHandler.ifPresent(eventHandler -> eventHandler.handle(event));
    }
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Optional<EventHandler> serverNewMemberEvent = getEventHandler(EventName.SERVER_NEW_MEMBER.getEventName());
        serverNewMemberEvent.ifPresent(eventHandler -> eventHandler.handle(event));
    }
    private Optional<EventHandler> getEventHandler(String eventHandlerName) {
        return eventHandlers.stream()
                .filter(handler -> handler.getName().equals(eventHandlerName))
                .findFirst();
    }
}
