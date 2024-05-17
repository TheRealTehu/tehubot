package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class SendGifCommandTest {
    private SendGifCommand sendGifCommand;
    private final TenorGifService mockTenorGifService = Mockito.mock(TenorGifService.class);
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final MessageCreateBuilder mockMessageCreateBuilder = Mockito.mock(MessageCreateBuilder.class);
    private final SlashCommandInteractionEvent mockEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final OptionMapping mockGifPromptOption = Mockito.mock(OptionMapping.class);
    private final OptionMapping mockChannelOption = Mockito.mock(OptionMapping.class);
    private final ReplyCallbackAction mockReplyCallback = Mockito.mock(ReplyCallbackAction.class);
    private final MessageEmbed mockMessageEmbed = Mockito.mock(MessageEmbed.class);
    private final MessageCreateData mockMessageCreateData = Mockito.mock(MessageCreateData.class);
    private final MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
    private final GuildChannelUnion mockGuildChannelUnion = Mockito.mock(GuildChannelUnion.class);
    private final TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
    private final InteractionHook mockInteractionHook = mock(InteractionHook.class);

    @BeforeEach
    void setup() {
        sendGifCommand = new SendGifCommand(mockTenorGifService, mockMessageSender, mockMessageCreateBuilder);
    }

    @Test
    void executeCommandWhenNoChannelWasGivenSendsGifToSameChannel() {
        when(mockEvent.getOption("gifprompt")).thenReturn(mockGifPromptOption);
        when(mockGifPromptOption.getAsString()).thenReturn("test");
        when(mockEvent.getOption("gifchannel")).thenReturn(null);
        when(mockEvent.deferReply()).thenReturn(mockReplyCallback);
        when(mockReplyCallback.setEphemeral(true)).thenReturn(mockReplyCallback);

        when(mockTenorGifService.getGifAsEmbed("test")).thenReturn(mockMessageEmbed);
        when(mockMessageCreateBuilder.addEmbeds(mockMessageEmbed)).thenReturn(mockMessageCreateBuilder);
        when(mockMessageCreateBuilder.build()).thenReturn(mockMessageCreateData);

        when(mockEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        when(mockEvent.getHook()).thenReturn(mockInteractionHook);

        sendGifCommand.executeCommand(mockEvent);

        verify(mockEvent).deferReply();
        verify(mockReplyCallback).setEphemeral(true);
        verify(mockReplyCallback).queue();

        verify(mockMessageSender).sendMessage(mockTextChannel, mockMessageCreateData);
        verify(mockMessageSender).sendMessageOnHook(mockInteractionHook, "Gif sent");
    }

    @Test
    void executeCommandWhenChannelWasGivenSendsGifToThatChannel() {
        TextChannel otherTextChannel = Mockito.mock(TextChannel.class);

        when(mockEvent.getOption("gifprompt")).thenReturn(mockGifPromptOption);
        when(mockGifPromptOption.getAsString()).thenReturn("test");
        when(mockEvent.getOption("gifchannel")).thenReturn(mockChannelOption);
        when(mockEvent.deferReply()).thenReturn(mockReplyCallback);
        when(mockReplyCallback.setEphemeral(true)).thenReturn(mockReplyCallback);

        when(mockTenorGifService.getGifAsEmbed("test")).thenReturn(mockMessageEmbed);
        when(mockMessageCreateBuilder.addEmbeds(mockMessageEmbed)).thenReturn(mockMessageCreateBuilder);
        when(mockMessageCreateBuilder.build()).thenReturn(mockMessageCreateData);

        when(mockEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        when(mockChannelOption.getAsChannel()).thenReturn(mockGuildChannelUnion);
        when(mockGuildChannelUnion.asTextChannel()).thenReturn(otherTextChannel);

        when(mockEvent.getHook()).thenReturn(mockInteractionHook);

        sendGifCommand.executeCommand(mockEvent);

        verify(mockEvent).deferReply();
        verify(mockReplyCallback).setEphemeral(true);
        verify(mockReplyCallback).queue();

        verify(mockMessageSender).sendMessage(otherTextChannel, mockMessageCreateData);
        verify(mockMessageSender).sendMessageOnHook(mockInteractionHook, "Gif sent");
    }
}