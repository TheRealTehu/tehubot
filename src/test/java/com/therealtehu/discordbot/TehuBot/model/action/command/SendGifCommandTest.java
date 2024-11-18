package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.SendGifData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.SendGifRepository;
import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
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

import java.util.Optional;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class SendGifCommandTest {
    private SendGifCommand sendGifCommand;
    @Mock
    private TenorGifService tenorGifServiceMock;
    @Mock
    private GuildRepository guildRepositoryMock;
    @Mock
    private SendGifRepository sendGifRepositoryMock;
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
    @Mock
    private Guild guildMock;
    @Mock
    private GuildData guildDataMock;
    @BeforeEach
    void setup() {
        sendGifCommand = new SendGifCommand(tenorGifServiceMock, messageSenderMock, messageCreateBuilderMock,
                guildRepositoryMock, sendGifRepositoryMock);
    }
        //TODO: Rewrite tests to check DB use. Add test when DB use fails. First test is ok, can copy parts
    @Test
    void executeCommandWhenNoChannelWasGivenAndUserHasPermissionAndGuildIsInDbSendsGifToSameChannelAndSavesToDb() {
        when(eventMock.deferReply()).thenReturn(replyCallbackMock);
        when(replyCallbackMock.setEphemeral(true)).thenReturn(replyCallbackMock);

        when(eventMock.getOption(OptionName.SEND_GIF_PROMPT_OPTION.getOptionName())).thenReturn(gifPromptOptionMock);
        when(gifPromptOptionMock.getAsString()).thenReturn("test");
        when(eventMock.getOption(OptionName.SEND_GIF_CHANNEL_OPTION.getOptionName())).thenReturn(null);

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(textChannelMock, Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));
        SendGifData expectedSendGifData = new SendGifData();
        expectedSendGifData.setSearchTerm("test");
        expectedSendGifData.setGuild(guildDataMock);

        when(tenorGifServiceMock.getGifAsEmbed("test")).thenReturn(messageEmbedMock);
        when(messageCreateBuilderMock.addEmbeds(messageEmbedMock)).thenReturn(messageCreateBuilderMock);
        when(messageCreateBuilderMock.build()).thenReturn(messageCreateDataMock);

        when(eventMock.getHook()).thenReturn(interactionHookMock);

        sendGifCommand.executeCommand(eventMock);

        verify(eventMock).deferReply();
        verify(replyCallbackMock).setEphemeral(true);
        verify(replyCallbackMock).queue();

        verify(guildRepositoryMock).findById(1L);
        verify(sendGifRepositoryMock).save(expectedSendGifData);

        verify(messageCreateBuilderMock).clear();

        verify(messageSenderMock).sendMessage(textChannelMock, messageCreateDataMock);
        verify(messageSenderMock).sendMessageOnHook(interactionHookMock, "Gif sent");
    }

    @Test
    void executeCommandWhenChannelWasGivenAndUserHasPermissionAmdGuildIsInDbSendsGifToThatChannelAndSavesToDb() {
        when(eventMock.deferReply()).thenReturn(replyCallbackMock);
        when(replyCallbackMock.setEphemeral(true)).thenReturn(replyCallbackMock);

        when(eventMock.getOption(OptionName.SEND_GIF_PROMPT_OPTION.getOptionName())).thenReturn(gifPromptOptionMock);
        when(gifPromptOptionMock.getAsString()).thenReturn("test");
        when(eventMock.getOption(OptionName.SEND_GIF_CHANNEL_OPTION.getOptionName())).thenReturn(channelOptionMock);

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        TextChannel otherTextChannelMock = Mockito.mock(TextChannel.class);
        when(channelOptionMock.getAsChannel()).thenReturn(guildChannelUnionMock);
        when(guildChannelUnionMock.asTextChannel()).thenReturn(otherTextChannelMock);

        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(otherTextChannelMock, Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));
        SendGifData expectedSendGifData = new SendGifData();
        expectedSendGifData.setSearchTerm("test");
        expectedSendGifData.setGuild(guildDataMock);

        when(tenorGifServiceMock.getGifAsEmbed("test")).thenReturn(messageEmbedMock);
        when(messageCreateBuilderMock.addEmbeds(messageEmbedMock)).thenReturn(messageCreateBuilderMock);
        when(messageCreateBuilderMock.build()).thenReturn(messageCreateDataMock);

        when(eventMock.getHook()).thenReturn(interactionHookMock);

        sendGifCommand.executeCommand(eventMock);

        verify(eventMock).deferReply();
        verify(replyCallbackMock).setEphemeral(true);
        verify(replyCallbackMock).queue();

        verify(guildRepositoryMock).findById(1L);
        verify(sendGifRepositoryMock).save(expectedSendGifData);

        verify(messageCreateBuilderMock).clear();

        verify(messageSenderMock).sendMessage(otherTextChannelMock, messageCreateDataMock);
        verify(messageSenderMock).sendMessageOnHook(interactionHookMock, "Gif sent");
    }

    @Test
    void executeCommandWhenNoChannelWasGivenAndUserDoesNotHavePermissionSendsErrorMessageAndDoesNotSaveToDb() {
        when(eventMock.deferReply()).thenReturn(replyCallbackMock);
        when(replyCallbackMock.setEphemeral(true)).thenReturn(replyCallbackMock);

        when(eventMock.getOption(OptionName.SEND_GIF_PROMPT_OPTION.getOptionName())).thenReturn(gifPromptOptionMock);
        when(gifPromptOptionMock.getAsString()).thenReturn("test");
        when(eventMock.getOption(OptionName.SEND_GIF_CHANNEL_OPTION.getOptionName())).thenReturn(null);

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(textChannelMock, Permission.MESSAGE_SEND)).thenReturn(false);

        sendGifCommand.executeCommand(eventMock);

        verify(eventMock).deferReply();
        verify(replyCallbackMock).setEphemeral(true);
        verify(replyCallbackMock).queue();

        verifyNoInteractions(tenorGifServiceMock);
        verifyNoInteractions(guildRepositoryMock);
        verifyNoInteractions(sendGifRepositoryMock);
        verify(messageSenderMock).replyToEventEphemeral(eventMock, "ERROR: Doesn't have permission to send message to channel!");

    }
    @Test
    void executeCommandWhenChannelWasGivenButUserDoesNotHavePermissionSendsErrorMessageAndDoesNotSaveToDb() {
        when(eventMock.deferReply()).thenReturn(replyCallbackMock);
        when(replyCallbackMock.setEphemeral(true)).thenReturn(replyCallbackMock);

        when(eventMock.getOption(OptionName.SEND_GIF_PROMPT_OPTION.getOptionName())).thenReturn(gifPromptOptionMock);
        when(gifPromptOptionMock.getAsString()).thenReturn("test");
        when(eventMock.getOption(OptionName.SEND_GIF_CHANNEL_OPTION.getOptionName())).thenReturn(channelOptionMock);

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
        verifyNoInteractions(guildRepositoryMock);
        verifyNoInteractions(sendGifRepositoryMock);
        verify(messageSenderMock).replyToEventEphemeral(eventMock, "ERROR: Doesn't have permission to send message to channel!");
    }

    @Test
    void executeCommandWhenNoChannelWasGivenAndUserHasPermissionAndGuildIsNotInDbSendsErrorMessageAndDoesNotSaveToDb() {
        when(eventMock.deferReply()).thenReturn(replyCallbackMock);
        when(replyCallbackMock.setEphemeral(true)).thenReturn(replyCallbackMock);

        when(eventMock.getOption(OptionName.SEND_GIF_PROMPT_OPTION.getOptionName())).thenReturn(gifPromptOptionMock);
        when(gifPromptOptionMock.getAsString()).thenReturn("test");
        when(eventMock.getOption(OptionName.SEND_GIF_CHANNEL_OPTION.getOptionName())).thenReturn(null);

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(messageChannelUnionMock.asTextChannel()).thenReturn(textChannelMock);

        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(textChannelMock, Permission.MESSAGE_SEND)).thenReturn(true);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        sendGifCommand.executeCommand(eventMock);

        verify(eventMock).deferReply();
        verify(replyCallbackMock).setEphemeral(true);
        verify(replyCallbackMock).queue();

        verify(guildRepositoryMock).findById(1L);
        verifyNoInteractions(sendGifRepositoryMock);

        verify(messageSenderMock).replyToEventEphemeral(eventMock, "DATABASE ERROR: Guild not found!");
        verifyNoMoreInteractions(messageSenderMock);
    }
}