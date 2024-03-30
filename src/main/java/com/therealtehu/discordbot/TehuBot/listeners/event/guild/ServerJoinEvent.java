package com.therealtehu.discordbot.TehuBot.listeners.event.guild;

import com.therealtehu.discordbot.TehuBot.model.button.ButtonWithFunctionality;
import com.therealtehu.discordbot.TehuBot.model.button.CreateChannelsForCategoriesButton;
import com.therealtehu.discordbot.TehuBot.model.button.SpecifyChannelsForCategoriesButton;
import com.therealtehu.discordbot.TehuBot.model.button.SpecifyOneChannelForAllButton;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServerJoinEvent {
    private static final String greetingText = "Hey! I'm TehuBot! I'm new on this server, a server admin please go through my initial setup!\n Where should I post?\n";
    private final List<ButtonWithFunctionality> buttons;

    @Autowired
    public ServerJoinEvent(ButtonWithFunctionality alwaysInCommandChannelButton,
                           ButtonWithFunctionality createOneChannelForAllButton,
                           CreateChannelsForCategoriesButton createChannelsForCategoriesButton,
                           SpecifyOneChannelForAllButton specifyOneChannelForAllButton,
                           SpecifyChannelsForCategoriesButton specifyChannelsForCategoriesButton) {
        buttons = new ArrayList<>();
        buttons.add(alwaysInCommandChannelButton);
        buttons.add(createOneChannelForAllButton);
        buttons.add(createChannelsForCategoriesButton);
        buttons.add(specifyOneChannelForAllButton);
        buttons.add(specifyChannelsForCategoriesButton);
    }
    public void setup(GuildJoinEvent event) {
        ActionRow actionRow = ActionRow.of(buttons);

        MessageCreateData messageCreateData = new MessageCreateBuilder()
                .addContent(greetingText)
                .addComponents(actionRow).build();
        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(messageCreateData).queue();
    }
}
