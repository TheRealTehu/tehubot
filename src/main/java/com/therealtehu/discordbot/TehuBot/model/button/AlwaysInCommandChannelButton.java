package com.therealtehu.discordbot.TehuBot.model.button;

import com.therealtehu.discordbot.TehuBot.listeners.event.guild.ButtonLabels;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import org.springframework.stereotype.Component;

@Component
public class AlwaysInCommandChannelButton extends ButtonWithFunctionality{
    private static final ButtonImpl button = (ButtonImpl) Button.secondary(ButtonLabels.SET_SERVER_DEFAULT.getButtonId(),
            "Always post to the channel where the command was given!");

    public AlwaysInCommandChannelButton() {
        super(button);
    }

    @Override
    public void handleClick(ButtonInteractionEvent event) {
        //TODO: IMPLEMENT
        throw new UnsupportedOperationException();
    }
}
