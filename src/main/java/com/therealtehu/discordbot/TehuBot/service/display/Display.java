package com.therealtehu.discordbot.TehuBot.service.display;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.stereotype.Service;

@Service
public class Display implements MessageSender{
    @Override
    public void sendMessage(TextChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

    @Override
    public void sendMessage(TextChannel channel, MessageCreateData message) {
        channel.sendMessage(message).queue();
    }

    @Override
    public void replyToEvent(SlashCommandInteractionEvent event, String message) {
        event.reply(message).queue();
    }

    @Override
    public void sendMessageOnHook(InteractionHook hook, String message) {
        hook.sendMessage(message).queue();
    }

    @Override
    public void sendMessageEmbedOnHook(InteractionHook hook, MessageEmbed embed) {
        hook.sendMessageEmbeds(embed).queue();
    }
}
