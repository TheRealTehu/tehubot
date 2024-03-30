package com.therealtehu.discordbot.TehuBot.listeners.event.guild;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.ArrayList;
import java.util.List;

public class ServerJoinEvent {
    private static final String greetingText = "Hey! I'm TehuBot! I'm new on this server, a server admin please go through my initial setup!\n Where should I post?\n";
    private static final Button alwaysInCommandChannelButton = Button.secondary(ButtonLabels.SET_SERVER_DEFAULT.getButtonId(),
            "Always post to the channel where the command was given!");
    private static final Button createOneChannelForAll = Button.secondary(ButtonLabels.CREATE_ONE_CHANNEL_FOR_ALL.getButtonId(),
            "Create one channel where everything should be posted!");
    private static final Button createChannelsForCategories = Button.secondary(ButtonLabels.CREATE_CHANNELS_FOR_CATEGORIES.getButtonId(),
            "Create channels for every category!");
    private static final Button specifyOneChannelForAll = Button.secondary(ButtonLabels.SPECIFY_ONE_CHANNEL_FOR_ALL.getButtonId(),
            "I will specify one channel where you should post!");
    private static final Button specifyChannelsForCategories = Button.secondary(ButtonLabels.SPECIFY_CHANNELS_FOR_CATEGORIES.getButtonId(),
            "I will specify channels for every category!");
    private static final List<Button> buttons = new ArrayList<>(){{
        add(alwaysInCommandChannelButton);
        add(createOneChannelForAll);
        add(createChannelsForCategories);
        add(specifyOneChannelForAll);
        add(specifyChannelsForCategories);
    }};
    public static void setup(GuildJoinEvent event) {
        ActionRow actionRow = ActionRow.of(buttons);

        MessageCreateData messageCreateData = new MessageCreateBuilder()
                .addContent(greetingText)
                .addComponents(actionRow).build();
        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(messageCreateData).queue();
    }
}
