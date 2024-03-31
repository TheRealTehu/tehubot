package com.therealtehu.discordbot.TehuBot.model.event;

public enum EventName {
    SERVER_JOIN("serverjoin"),
    SERVER_NEW_MEMBER("servernewmember");

    private final String eventName;

    EventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
