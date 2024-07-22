package com.therealtehu.discordbot.TehuBot.model.action.event;

import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;

public interface DropDownEvent {
    boolean canHandleEvent(String componentId);
}
