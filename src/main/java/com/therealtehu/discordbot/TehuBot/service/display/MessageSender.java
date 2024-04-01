package com.therealtehu.discordbot.TehuBot.service.display;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public interface MessageSender {
    void sendMessage(TextChannel channel, String message);
}
