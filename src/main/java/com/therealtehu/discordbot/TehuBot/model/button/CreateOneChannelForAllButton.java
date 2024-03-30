package com.therealtehu.discordbot.TehuBot.model.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import org.springframework.stereotype.Component;

@Component
public class CreateOneChannelForAllButton extends ButtonWithFunctionality{
    private static final ButtonImpl button = (ButtonImpl) Button.secondary(ButtonLabel.CREATE_ONE_CHANNEL_FOR_ALL.getButtonId(),
            "Create one channel where everything should be posted!");

    public CreateOneChannelForAllButton() {
        super(button);
    }

    @Override
    public void handleClick(ButtonInteractionEvent event) {
        //TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }
}
