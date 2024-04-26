package com.therealtehu.discordbot.TehuBot.listeners.event;

import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventName;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventListenerTest {
    private EventListener eventListener;

    private final EventHandler mockEventHandler = Mockito.mock(EventHandler.class);

    private final Logger mockLogger = Mockito.mock(Logger.class);
    @Test
    void onGuildJoinWhenEventHandlerListIsEmptyLogsError() {
        eventListener = new EventListener(List.of(), mockLogger);
        GuildJoinEvent mockJoinEvent = Mockito.mock(GuildJoinEvent.class);

        eventListener.onGuildJoin(mockJoinEvent);

        verify(mockLogger).error("No suitable EventHandler for: " + EventName.SERVER_JOIN.getEventName());
    }

    @Test
    void onGuildJoinWhenHasOneEventHandlerButNotTheNeededEventHandlerLogsError() {
        eventListener = new EventListener(List.of(mockEventHandler), mockLogger);
        GuildJoinEvent mockJoinEvent = Mockito.mock(GuildJoinEvent.class);

        when(mockEventHandler.getName()).thenReturn("otherhandler");

        eventListener.onGuildJoin(mockJoinEvent);

        verify(mockLogger).error("No suitable EventHandler for: " + EventName.SERVER_JOIN.getEventName());
    }

    @Test
    void onGuildJoinWhenHasOneEventHandlerAndItIsTheNeededEventHandlerExecutesEvent() {
        eventListener = new EventListener(List.of(mockEventHandler), mockLogger);
        GuildJoinEvent mockJoinEvent = Mockito.mock(GuildJoinEvent.class);

        when(mockEventHandler.getName()).thenReturn(EventName.SERVER_JOIN.getEventName());

        eventListener.onGuildJoin(mockJoinEvent);

        verify(mockEventHandler).handle(mockJoinEvent);
    }

    @Test
    void onGuildJoinWhenHasTwoEventHandlerAndOneIsTheNeededEventHandlerExecutesEvent() {
        EventHandler otherMockEventHandler = Mockito.mock(EventHandler.class);
        eventListener = new EventListener(List.of(mockEventHandler, otherMockEventHandler), mockLogger);
        GuildJoinEvent mockJoinEvent = Mockito.mock(GuildJoinEvent.class);

        when(mockEventHandler.getName()).thenReturn(EventName.SERVER_JOIN.getEventName());
        when(otherMockEventHandler.getName()).thenReturn("otherhandler");

        eventListener.onGuildJoin(mockJoinEvent);

        verify(mockEventHandler).handle(mockJoinEvent);
    }
    @Test
    void onGuildMemberJoinWhenEventHandlerListIsEmptyLogsError() {
        eventListener = new EventListener(List.of(), mockLogger);
        GuildMemberJoinEvent mockJoinEvent = Mockito.mock(GuildMemberJoinEvent.class);

        eventListener.onGuildMemberJoin(mockJoinEvent);

        verify(mockLogger).error("No suitable EventHandler for: " + EventName.SERVER_NEW_MEMBER.getEventName());
    }

    @Test
    void onGuildMemberJoinWhenHasOneEventHandlerButNotTheNeededEventHandlerLogsError() {
        eventListener = new EventListener(List.of(mockEventHandler), mockLogger);
        GuildMemberJoinEvent mockJoinEvent = Mockito.mock(GuildMemberJoinEvent.class);

        when(mockEventHandler.getName()).thenReturn("otherhandler");

        eventListener.onGuildMemberJoin(mockJoinEvent);

        verify(mockLogger).error("No suitable EventHandler for: " + EventName.SERVER_NEW_MEMBER.getEventName());
    }

    @Test
    void onGuildMemberJoinWhenHasOneEventHandlerAndItIsTheNeededEventHandlerExecutesEvent() {
        eventListener = new EventListener(List.of(mockEventHandler), mockLogger);
        GuildMemberJoinEvent mockJoinEvent = Mockito.mock(GuildMemberJoinEvent.class);

        when(mockEventHandler.getName()).thenReturn(EventName.SERVER_NEW_MEMBER.getEventName());

        eventListener.onGuildMemberJoin(mockJoinEvent);

        verify(mockEventHandler).handle(mockJoinEvent);
    }

    @Test
    void onGuildMemberJoinWhenHasTwoEventHandlerAndOneIsTheNeededEventHandlerExecutesEvent() {
        EventHandler otherMockEventHandler = Mockito.mock(EventHandler.class);
        eventListener = new EventListener(List.of(mockEventHandler, otherMockEventHandler), mockLogger);
        GuildMemberJoinEvent mockJoinEvent = Mockito.mock(GuildMemberJoinEvent.class);

        when(mockEventHandler.getName()).thenReturn(EventName.SERVER_NEW_MEMBER.getEventName());
        when(otherMockEventHandler.getName()).thenReturn("otherhandler");

        eventListener.onGuildMemberJoin(mockJoinEvent);

        verify(mockEventHandler).handle(mockJoinEvent);
    }
}