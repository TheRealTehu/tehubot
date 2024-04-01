package com.therealtehu.discordbot.TehuBot.model.event;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.events.Event;

public abstract class EventHandler {
    private final String name;
    protected final MessageSender messageSender;
    protected EventHandler(String name, MessageSender messageSender) {
        this.name = name;
        this.messageSender = messageSender;
    }

    public String getName() {
        return name;
    }

    public abstract void handle(Event event);
}
