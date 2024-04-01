package com.therealtehu.discordbot.TehuBot.model.event.guild;

import com.therealtehu.discordbot.TehuBot.model.button.*;
import com.therealtehu.discordbot.TehuBot.model.button.setup.*;
import com.therealtehu.discordbot.TehuBot.model.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.event.EventName;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
    private static final String GREETING_TEXT = "Hey! I'm TehuBot! I'm new on this server, a server admin please go through my initial setup!\n Where should I post?\n";
    private final List<ButtonWithFunctionality> buttons;

    @Autowired
    public ServerJoinEvent(MessageSender messageSender) {
        super(EventName.SERVER_JOIN.getEventName(), messageSender);
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

            TextChannel channel = guildJoinEvent.getGuild().getDefaultChannel().asTextChannel();
            MessageCreateData messageCreateData = new MessageCreateBuilder()
                    .addContent(GREETING_TEXT)
                    .addComponents(actionRow).build();
            messageSender.sendMessage(channel, messageCreateData);
        }
    }
}
