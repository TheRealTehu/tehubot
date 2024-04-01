package com.therealtehu.discordbot.TehuBot.service.display;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;

@Service
public class Display implements MessageSender{

    @Override
    public void sendMessage(TextChannel channel, String message) {
        channel.sendMessage(message).queue();
    }
}
