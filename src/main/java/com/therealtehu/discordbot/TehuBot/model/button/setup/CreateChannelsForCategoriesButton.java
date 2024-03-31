package com.therealtehu.discordbot.TehuBot.model.button.setup;

import com.therealtehu.discordbot.TehuBot.model.button.ButtonLabel;
import com.therealtehu.discordbot.TehuBot.model.button.ButtonWithFunctionality;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import org.springframework.stereotype.Component;

@Component
public class CreateChannelsForCategoriesButton extends ButtonWithFunctionality {
    private static final ButtonImpl BUTTON = (ButtonImpl) Button.secondary(ButtonLabel.CREATE_CHANNELS_FOR_CATEGORIES.getButtonId(),
            "Create channels for every category!");

    public CreateChannelsForCategoriesButton() {
        super(BUTTON);
    }

    @Override
    public void handleClick(ButtonInteractionEvent event) {
        //TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }
}
