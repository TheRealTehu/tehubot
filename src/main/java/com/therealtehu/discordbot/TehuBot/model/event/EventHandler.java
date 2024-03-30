package com.therealtehu.discordbot.TehuBot.model.event;

import net.dv8tion.jda.api.events.Event;

public abstract class EventHandler {
    private final String name;

    protected EventHandler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void handle(Event event);
}
