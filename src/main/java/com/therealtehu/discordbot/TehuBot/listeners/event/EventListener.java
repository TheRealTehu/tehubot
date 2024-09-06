package com.therealtehu.discordbot.TehuBot.listeners.event;

import com.therealtehu.discordbot.TehuBot.model.action.event.DropDownEvent;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventName;
import com.therealtehu.discordbot.TehuBot.model.action.event.poll.MessageReactionEventWithText;
import com.therealtehu.discordbot.TehuBot.service.display.Display;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventListener extends ListenerAdapter {
    private final List<EventHandler> eventHandlers;
    private final Display messageSender;

    @Autowired
    public EventListener(List<EventHandler> eventHandlers, Display messageSender) {
        this.eventHandlers = eventHandlers;
        this.messageSender = messageSender;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Optional<EventHandler> serverJoinEventHandler = getEventHandler(EventName.SERVER_JOIN.getEventName());
        serverJoinEventHandler.ifPresentOrElse(eventHandler -> eventHandler.handle(event),
                sendErrorMessage(EventName.SERVER_JOIN.getEventName(), event.getGuild().getDefaultChannel().asTextChannel()));
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Optional<EventHandler> serverNewMemberEvent = getEventHandler(EventName.SERVER_NEW_MEMBER.getEventName());
        serverNewMemberEvent.ifPresentOrElse(eventHandler -> eventHandler.handle(event),
                sendErrorMessage(EventName.SERVER_NEW_MEMBER.getEventName(), event.getGuild().getDefaultChannel().asTextChannel()));
    }

    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
        Optional<EventHandler> dropDownEvent = getDropDownEventHandler(event.getComponentId());
        dropDownEvent.ifPresentOrElse(eventHandler -> eventHandler.handle(event),
                sendErrorMessage(EventName.CHANNEL_CHOOSING_DROPDOWN.getEventName(), event.getChannel().asTextChannel()));
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        event.retrieveMessage().queue(message -> {
            if(message.getContentRaw().startsWith("__poll id:__")) {
                Optional<EventHandler> pollVoteEvent = getEventHandler(EventName.POLL_VOTE.getEventName());
                pollVoteEvent.ifPresentOrElse(eventHandler ->
                                eventHandler.handle(new MessageReactionEventWithText(event, message.getContentRaw())),
                        sendErrorMessage(EventName.POLL_VOTE.getEventName(), event.getChannel().asTextChannel()));
            }
        });
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        //TODO
        if (!event.getUser().isBot()) {
            super.onMessageReactionRemove(event);
            throw new UnsupportedOperationException();
        }
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

    private Runnable sendErrorMessage(String missingEventHandler, TextChannel channel) {
        return () -> messageSender.sendMessage(channel, "No suitable EventHandler for: " + missingEventHandler);
    }
}
