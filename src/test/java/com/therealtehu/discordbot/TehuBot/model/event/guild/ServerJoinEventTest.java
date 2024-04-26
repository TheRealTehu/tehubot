package com.therealtehu.discordbot.TehuBot.model.event.guild;

import com.therealtehu.discordbot.TehuBot.model.action.event.guild.ServerJoinEvent;
import com.therealtehu.discordbot.TehuBot.model.button.ButtonWithFunctionality;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerJoinEventTest {

    private List<ButtonWithFunctionality> buttons;

    private ServerJoinEvent serverJoinEvent;

    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);

    private final GuildJoinEvent mockGuildJoinEvent = Mockito.mock(GuildJoinEvent.class);

    private final Guild mockGuild = Mockito.mock(Guild.class);

    private final DefaultGuildChannelUnion mockDefaultGuildChannelOpinion = Mockito.mock(DefaultGuildChannelUnion.class);

    private final TextChannel mockTextChannel = Mockito.mock(TextChannel.class);

    @Test
    void handle() {
    }
}