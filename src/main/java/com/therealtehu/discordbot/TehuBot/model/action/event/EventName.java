package com.therealtehu.discordbot.TehuBot.model.action.event;

public enum EventName {
    SERVER_JOIN("serverjoin"),
    SERVER_NEW_MEMBER("servernewmember"),
    CHANNEL_CHOOSING_DROPDOWN("channelchossingdropdown");

    private final String eventName;

    EventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
