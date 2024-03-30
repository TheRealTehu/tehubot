package com.therealtehu.discordbot.TehuBot.model.button.setup;

import com.therealtehu.discordbot.TehuBot.model.button.ButtonLabel;
import com.therealtehu.discordbot.TehuBot.model.button.ButtonWithFunctionality;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import org.springframework.stereotype.Component;

@Component
public class SpecifyOneChannelForAllButton extends ButtonWithFunctionality {
    private static final ButtonImpl button = (ButtonImpl) Button.secondary(ButtonLabel.SPECIFY_ONE_CHANNEL_FOR_ALL.getButtonId(),
            "I will specify one channel where you should post!");

    public SpecifyOneChannelForAllButton() {
        super(button);
    }

    @Override
    public void handleClick(ButtonInteractionEvent event) {
        //TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }
}
