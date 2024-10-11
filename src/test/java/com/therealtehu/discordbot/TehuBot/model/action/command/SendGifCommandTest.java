package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class SendGifCommandTest {
    private SendGifCommand sendGifCommand;
    @Mock
    private TenorGifService tenorGifServiceMock;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private MessageCreateBuilder messageCreateBuilderMock;
    @Mock
    private SlashCommandInteractionEvent eventMock;
    @Mock
    private OptionMapping gifPromptOptionMock;
    @Mock
    private OptionMapping channelOptionMock;
    @Mock
    private ReplyCallbackAction replyCallbackMock;
    @Mock
    private MessageEmbed messageEmbedMock;
    @Mock
    private MessageCreateData messageCreateDataMock;
    @Mock
    private MessageChannelUnion messageChannelUnionMock;
    @Mock
    private GuildChannelUnion guildChannelUnionMock;
    @Mock
    private TextChannel textChannelMock;
    @Mock
    private InteractionHook interactionHookMock;
    @Mock
    private Member memberMock;
    @BeforeEach
    void setup() {
        sendGifCommand = new SendGifCommand(tenorGifServiceMock, messageSenderMock, messageCreateBuilderMock);
    }

    @Test
    void executeCommandWhenNoChannelWasGivenAndUserHasPermissionSendsGifToSameChannel() {
        when(eventMock.getOption("gifprompt")).thenReturn(gifPromptOptionMock);
        when(gifPromptOptionMock.getAsString()).thenReturn("test");
        when(eventMock.getOption("gifchannel")).thenReturn(null);
        when(eventMock.deferReply()).thenReturn(replyCallbackMock);
        when(replyCallbackMock.setEphemeral(true)).thenReturn(replyCallbackMock);

        when(tenorGifServiceMock.getGifAsEmbed("test")).thenReturn(messageEmbedMock);
        when(messageCreateBuilderMock.addEmbeds(messageEmbedMock)).thenReturn(messageCreateBuilderMock);
        when(messageCreateBuilderMock.build()).thenReturn(messageCreateDataMock);

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(textChannelMock, Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.getHook()).thenReturn(interactionHookMock);

        sendGifCommand.executeCommand(eventMock);

        verify(eventMock).deferReply();
        verify(replyCallbackMock).setEphemeral(true);
        verify(replyCallbackMock).queue();

        verify(messageSenderMock).sendMessage(textChannelMock, messageCreateDataMock);
        verify(messageSenderMock).sendMessageOnHook(interactionHookMock, "Gif sent");
    }

    @Test
    void executeCommandWhenChannelWasGivenAndUserHasPermissionSendsGifToThatChannel() {
        TextChannel otherTextChannel = Mockito.mock(TextChannel.class);

        when(eventMock.getOption("gifprompt")).thenReturn(gifPromptOptionMock);
        when(gifPromptOptionMock.getAsString()).thenReturn("test");
        when(eventMock.getOption("gifchannel")).thenReturn(channelOptionMock);
        when(eventMock.deferReply()).thenReturn(replyCallbackMock);
        when(replyCallbackMock.setEphemeral(true)).thenReturn(replyCallbackMock);

        when(tenorGifServiceMock.getGifAsEmbed("test")).thenReturn(messageEmbedMock);
        when(messageCreateBuilderMock.addEmbeds(messageEmbedMock)).thenReturn(messageCreateBuilderMock);
        when(messageCreateBuilderMock.build()).thenReturn(messageCreateDataMock);

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(channelOptionMock.getAsChannel()).thenReturn(guildChannelUnionMock);
        when(guildChannelUnionMock.asTextChannel()).thenReturn(otherTextChannel);

        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(otherTextChannel, Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.getHook()).thenReturn(interactionHookMock);

        sendGifCommand.executeCommand(eventMock);

        verify(eventMock).deferReply();
        verify(replyCallbackMock).setEphemeral(true);
        verify(replyCallbackMock).queue();

        verify(messageSenderMock).sendMessage(otherTextChannel, messageCreateDataMock);
        verify(messageSenderMock).sendMessageOnHook(interactionHookMock, "Gif sent");
    }

    @Test
    void executeCommandWhenNoChannelWasGivenAndUserDoesNotHavePermissionSendsErrorMessage() {
        when(eventMock.getOption("gifprompt")).thenReturn(gifPromptOptionMock);
        when(gifPromptOptionMock.getAsString()).thenReturn("test");
        when(eventMock.getOption("gifchannel")).thenReturn(null);
        when(eventMock.deferReply()).thenReturn(replyCallbackMock);
        when(replyCallbackMock.setEphemeral(true)).thenReturn(replyCallbackMock);

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(textChannelMock, Permission.MESSAGE_SEND)).thenReturn(false);

        sendGifCommand.executeCommand(eventMock);

        verify(eventMock).deferReply();
        verify(replyCallbackMock).setEphemeral(true);
        verify(replyCallbackMock).queue();

        verifyNoInteractions(tenorGifServiceMock);
        verify(messageSenderMock).replyToEventEphemeral(eventMock, "ERROR: Doesn't have permission to send message to channel!");

    }
    @Test
    void executeCommandWhenChannelWasGivenButUserDoesNotHavePermissionSendsErrorMessage() {
        when(eventMock.getOption("gifprompt")).thenReturn(gifPromptOptionMock);
        when(gifPromptOptionMock.getAsString()).thenReturn("test");
        when(eventMock.getOption("gifchannel")).thenReturn(channelOptionMock);
        when(eventMock.deferReply()).thenReturn(replyCallbackMock);
        when(replyCallbackMock.setEphemeral(true)).thenReturn(replyCallbackMock);

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(channelOptionMock.getAsChannel()).thenReturn(guildChannelUnionMock);
        when(guildChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(textChannelMock, Permission.MESSAGE_SEND)).thenReturn(false);

        sendGifCommand.executeCommand(eventMock);

        verify(eventMock).deferReply();
        verify(replyCallbackMock).setEphemeral(true);
        verify(replyCallbackMock).queue();

        verifyNoInteractions(tenorGifServiceMock);
        verify(messageSenderMock).replyToEventEphemeral(eventMock, "ERROR: Doesn't have permission to send message to channel!");
    }
}