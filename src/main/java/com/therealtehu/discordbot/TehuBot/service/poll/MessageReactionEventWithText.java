package com.therealtehu.discordbot.TehuBot.service.poll;

import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

public class MessageReactionEventWithText extends GenericMessageReactionEvent {
    private final String message;

    public MessageReactionEventWithText(GenericMessageReactionEvent event, String message) {
        super(event.getJDA(), event.getResponseNumber(), event.getUser(), event.getMember(),
                event.getReaction(), event.getUserIdLong());
        this.message = message;
    }

    public String getImmediateMessage() {
        return message;
    }
}
