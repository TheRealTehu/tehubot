package com.therealtehu.discordbot.TehuBot.model.button;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;

public abstract class ButtonWithFunctionality extends ButtonImpl {
    public ButtonWithFunctionality(DataObject data) {
        super(data);
    }

    public ButtonWithFunctionality(String id, String label, ButtonStyle style, boolean disabled, Emoji emoji) {
        super(id, label, style, disabled, emoji);
    }

    public ButtonWithFunctionality(String id, String label, ButtonStyle style, String url, boolean disabled, Emoji emoji) {
        super(id, label, style, url, disabled, emoji);
    }

    public ButtonWithFunctionality(ButtonImpl button) {
        super(button.toData());
    }

    public abstract void handleClick(ButtonInteractionEvent event);
}
