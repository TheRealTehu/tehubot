package com.therealtehu.discordbot.TehuBot.service.display;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public interface MessageSender {
    void sendMessage(TextChannel channel, String message);
    void sendMessage(TextChannel channel, MessageCreateData message);
    void sendMessageWithMessageEmbed(TextChannel channel, String message, MessageEmbed gif);
    void reply(IReplyCallback event, String message);
    void replyAndDeleteMessage(IReplyCallback event, String message, MessageChannelUnion channel, Long messageId);
    void replyToEventEphemeral(SlashCommandInteractionEvent event, String message);
    void sendMessageOnHook(InteractionHook hook, String message);
    void sendMessageEmbedOnHook(InteractionHook hook, MessageEmbed embed);

}
