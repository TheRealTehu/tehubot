package com.therealtehu.discordbot.TehuBot.model.action.event.guild;

import com.therealtehu.discordbot.TehuBot.model.button.ButtonWithFunctionality;
import com.therealtehu.discordbot.TehuBot.model.button.setup.AlwaysInCommandChannelButton;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServerJoinEventTest {
    private static final String GREETING_TEXT = "Hey! I'm TehuBot! I'm new on this server, a server admin please go through my initial setup!\n Where should I post?\n";
    private final List<ButtonWithFunctionality> buttons = List.of(new AlwaysInCommandChannelButton());
    private ServerJoinEvent serverJoinEvent;
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final GuildJoinEvent mockGuildJoinEvent = Mockito.mock(GuildJoinEvent.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final DefaultGuildChannelUnion mockDefaultGuildChannelUnion = Mockito.mock(DefaultGuildChannelUnion.class);
    private final TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
    private final MessageCreateBuilder mockMessageCreateBuilder = Mockito.mock(MessageCreateBuilder.class);
    private final MessageCreateData mockMessageCreateData = Mockito.mock(MessageCreateData.class);
    @Test
    void handleServerJoin() {
        serverJoinEvent = new ServerJoinEvent(buttons, mockMessageSender, mockMessageCreateBuilder);

        when(mockGuildJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockDefaultGuildChannelUnion);
        when(mockDefaultGuildChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        when(mockMessageCreateBuilder.addContent(GREETING_TEXT)).thenReturn(mockMessageCreateBuilder);
        ActionRow actionRow = ActionRow.of(buttons);
        when(mockMessageCreateBuilder.addComponents(actionRow)).thenReturn(mockMessageCreateBuilder);
        when(mockMessageCreateBuilder.build()).thenReturn(mockMessageCreateData);

        serverJoinEvent.handle(mockGuildJoinEvent);

        verify(mockMessageSender).sendMessage(mockTextChannel, mockMessageCreateData);
    }
}