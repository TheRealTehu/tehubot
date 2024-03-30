package com.therealtehu.discordbot.TehuBot.model.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import org.springframework.stereotype.Component;

@Component
public class SpecifyChannelsForCategoriesButton extends ButtonWithFunctionality{
    private static final ButtonImpl button = (ButtonImpl) Button.secondary(ButtonLabel.SPECIFY_CHANNELS_FOR_CATEGORIES.getButtonId(),
            "I will specify channels for every category!");

    public SpecifyChannelsForCategoriesButton() {
        super(button);
    }

    @Override
    public void handleClick(ButtonInteractionEvent event) {
        //TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }
}
