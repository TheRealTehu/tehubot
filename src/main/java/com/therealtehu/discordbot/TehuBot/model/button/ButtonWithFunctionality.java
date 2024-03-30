package com.therealtehu.discordbot.TehuBot.model.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;

public abstract class ButtonWithFunctionality extends ButtonImpl {
    public ButtonWithFunctionality(ButtonImpl button) {
        super(button.toData());
    }

    public abstract void handleClick(ButtonInteractionEvent event);
}
