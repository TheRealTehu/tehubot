package com.therealtehu.discordbot.TehuBot.model.action.event;

public enum EventName {
    SERVER_JOIN("serverjoin"),
    SERVER_NEW_MEMBER("servernewmember"),
    CHANNEL_CHOOSING_DROPDOWN("channelchossingdropdown"),
    POLL_VOTE("pollvote"),
    POLL_REMOVE_VOTE("pollremovevote");

    private final String eventName;

    EventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
