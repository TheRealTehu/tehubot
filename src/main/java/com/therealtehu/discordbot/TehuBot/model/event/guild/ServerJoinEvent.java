package com.therealtehu.discordbot.TehuBot.model.event.guild;

import com.therealtehu.discordbot.TehuBot.model.button.*;
import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.EventName;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServerJoinEvent extends EventHandler {
    private static final String greetingText = "Hey! I'm TehuBot! I'm new on this server, a server admin please go through my initial setup!\n Where should I post?\n";
    private final List<ButtonWithFunctionality> buttons;

    @Autowired
    public ServerJoinEvent() {
        super(EventName.SERVER_JOIN.getEventName());
        buttons = new ArrayList<>();
        buttons.add(new AlwaysInCommandChannelButton());
        buttons.add(new CreateOneChannelForAllButton());
        buttons.add(new CreateChannelsForCategoriesButton());
        buttons.add(new SpecifyOneChannelForAllButton());
        buttons.add(new SpecifyChannelsForCategoriesButton());
    }
    @Override
    public void handle(Event event) {
        if(event instanceof GuildJoinEvent guildJoinEvent) {
            ActionRow actionRow = ActionRow.of(buttons);

            MessageCreateData messageCreateData = new MessageCreateBuilder()
                    .addContent(greetingText)
                    .addComponents(actionRow).build();
            guildJoinEvent.getGuild().getDefaultChannel().asTextChannel().sendMessage(messageCreateData).queue();
        }
    }
}
