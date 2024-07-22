package com.therealtehu.discordbot.TehuBot.model.action.event.guild.server_join;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.model.action.event.DropDownEvent;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventName;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelChoosingDropDownEvent extends EventHandler implements DropDownEvent {

    private final GuildRepository guildRepository;

    @Autowired
    public ChannelChoosingDropDownEvent(MessageSender messageSender, GuildRepository guildRepository) {
        super(EventName.CHANNEL_CHOOSING_DROPDOWN.getEventName(), messageSender);
        this.guildRepository = guildRepository;
    }

    @Override
    public void handle(Event event) {
        if(event instanceof EntitySelectInteractionEvent dropDownEvent) {
            if (dropDownEvent.getComponentId().equals(ServerJoinEvent.DROP_DOWN_EVENT_ID)) {
                GuildChannel channel = dropDownEvent.getMentions().getChannels().getFirst();
                GuildData guildData = new GuildData();
                guildData.setBotChatChannelId(channel.getIdLong());

                guildRepository.save(guildData);
            }

        }
    }

    @Override
    public boolean canHandleEvent(String componentId) {
        return ServerJoinEvent.DROP_DOWN_EVENT_ID.equals(componentId);
    }
}
