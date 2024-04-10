package com.therealtehu.discordbot.TehuBot.listeners.event;

import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class EventListenerTest {
    private EventListener eventListener;

    private final EventHandler mockEventHandler = Mockito.mock(EventHandler.class);

    @Test
    void onGuildJoin() {

    }

    @Test
    void onGuildMemberJoin() {
    }
}