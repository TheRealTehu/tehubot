package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.TenorGifService;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class SendGifCommandTest {
    private SendGifCommand sendGifCommand;

    private TenorGifService mockTenorGifService = Mockito.mock(TenorGifService.class);
    private MessageSender mockMessageSender = Mockito.mock(MessageSender.class);

    private SlashCommandInteractionEvent mockEvent = Mockito.mock(SlashCommandInteractionEvent.class);

    private OptionMapping mockGifPromptOption = Mockito.mock(OptionMapping.class);
    private OptionMapping mockChannelOption = Mockito.mock(OptionMapping.class);
    private ReplyCallbackAction mockReplyCallback = Mockito.mock(ReplyCallbackAction.class);
    private MessageEmbed mockMessageEmbed = Mockito.mock(MessageEmbed.class);
    private MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
    private TextChannel mockTextChannel = Mockito.mock(TextChannel.class);

    @BeforeEach
    void setup() {
        sendGifCommand = new SendGifCommand(mockTenorGifService, mockMessageSender);
    }

    @Test
    void executeCommand() {
    }
}