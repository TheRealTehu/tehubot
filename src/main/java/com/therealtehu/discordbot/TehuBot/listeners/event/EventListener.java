package com.therealtehu.discordbot.TehuBot.listeners.event;

import com.therealtehu.discordbot.TehuBot.model.action.event.DropDownEvent;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventName;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventListener extends ListenerAdapter {
    private final List<EventHandler> eventHandlers;

    private final Logger logger;

    @Autowired
    public EventListener(List<EventHandler> eventHandlers, Logger logger) {
        this.eventHandlers = eventHandlers;
        this.logger = logger;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Optional<EventHandler> serverJoinEventHandler = getEventHandler(EventName.SERVER_JOIN.getEventName());
        serverJoinEventHandler.ifPresentOrElse(eventHandler -> eventHandler.handle(event),
                sendErrorMessage(EventName.SERVER_JOIN.getEventName()));
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Optional<EventHandler> serverNewMemberEvent = getEventHandler(EventName.SERVER_NEW_MEMBER.getEventName());
        serverNewMemberEvent.ifPresentOrElse(eventHandler -> eventHandler.handle(event),
                sendErrorMessage(EventName.SERVER_NEW_MEMBER.getEventName()));
    }

    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
        Optional<EventHandler> dropDownEvent = getDropDownEventHandler(event.getComponentId());
        dropDownEvent.ifPresentOrElse(eventHandler -> eventHandler.handle(event),
                sendErrorMessage(EventName.CHANNEL_CHOOSING_DROPDOWN.getEventName()));
    }

    private Optional<EventHandler> getEventHandler(String eventHandlerName) {
        return eventHandlers.stream()
                .filter(handler -> handler.getName().equals(eventHandlerName))
                .findFirst();
    }

    private Optional<EventHandler> getDropDownEventHandler(String componentId) {
        return eventHandlers.stream()
                .filter(handler -> handler instanceof DropDownEvent dropDownEvent
                        && dropDownEvent.canHandleEvent(componentId))
                .findFirst();
    }

    private Runnable sendErrorMessage(String missingEventHandler) {
        return () -> logger.error("No suitable EventHandler for: " + missingEventHandler);
    }
}
