package com.therealtehu.discordbot.TehuBot.model.action.event.guild.server_join;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Mentions;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChannelChoosingDropDownEventTest {
    private ChannelChoosingDropDownEvent channelChoosingDropDownEvent;
    @Mock
    private GuildRepository guildRepositoryMock;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private EntitySelectInteractionEvent eventMock;
    @Mock
    private Member memberMock;
    @Mock
    private Mentions mentionsMock;
    @Mock
    private GuildChannel guildChannelMock;
    @Mock
    private Guild guildMock;
    @Mock
    private MessageChannelUnion messageChannelUnionMock;

    @BeforeEach
    void setup() {
        channelChoosingDropDownEvent = new ChannelChoosingDropDownEvent(messageSenderMock, guildRepositoryMock);
    }

    @Test
    void handleEventWhenComponentIdIsForTheEventAndUserHasPermissionAndGuildIsInDbUpdatesDbAndRepliesToEventAndDeletesEventMessage() {
        when(eventMock.getComponentId()).thenReturn("choose-bot-channel");
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MANAGE_SERVER)).thenReturn(true);

        when(eventMock.getMentions()).thenReturn(mentionsMock);
        when(mentionsMock.getChannels()).thenReturn(List.of(guildChannelMock));

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        GuildData actualGuildData = new GuildData();
        actualGuildData.setId(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(actualGuildData));
        when(guildChannelMock.getIdLong()).thenReturn(100L);

        GuildData expectedGuildData = new GuildData();
        expectedGuildData.setId(1L);
        expectedGuildData.setBotChatChannelId(100L);

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(eventMock.getMessageIdLong()).thenReturn(30L);

        channelChoosingDropDownEvent.handle(eventMock);

        verify(guildRepositoryMock).save(expectedGuildData);
        verify(messageSenderMock).replyAndDeleteMessage(eventMock, "Setup finished!",
                messageChannelUnionMock, 30L);
    }

    @Test
    void handleEventWhenComponentIdIsForTheEventAndUserHasPermissionAndGuildIsNotInDbRepliesToEventWithErrorMessageAndDeletesEventMessage() {
        when(eventMock.getComponentId()).thenReturn("choose-bot-channel");
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MANAGE_SERVER)).thenReturn(true);

        when(eventMock.getMentions()).thenReturn(mentionsMock);
        when(mentionsMock.getChannels()).thenReturn(List.of(guildChannelMock));

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        when(eventMock.getChannel()).thenReturn(messageChannelUnionMock);
        when(eventMock.getMessageIdLong()).thenReturn(30L);

        channelChoosingDropDownEvent.handle(eventMock);

        verify(guildRepositoryMock, times(0)).save(any());
        verify(messageSenderMock).replyAndDeleteMessage(eventMock, "ERROR: Guild not found in database!",
                messageChannelUnionMock, 30L);
    }

    @Test
    void handleEventWhenComponentIdIsForTheEventAndUserDoesNotHavePermissionRepliesToEventWithErrorMessage() {
        when(eventMock.getComponentId()).thenReturn("choose-bot-channel");
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MANAGE_SERVER)).thenReturn(false);

        channelChoosingDropDownEvent.handle(eventMock);

        verify(guildRepositoryMock, times(0)).save(any());
        verify(messageSenderMock).reply(eventMock, "ERROR: Doesn't have permission to setup guild!");
    }

    @Test
    void handleEventWhenComponentIdIsNotForTheEventNothingHappens() {
        when(eventMock.getComponentId()).thenReturn("not-choose-bot-channel-id");

        channelChoosingDropDownEvent.handle(eventMock);

        verifyNoInteractions(guildRepositoryMock);
        verifyNoInteractions(messageSenderMock);
    }

    @Test
    void canHandleEventWhenComponentIdIsForTheEventReturnsTrue() {
        assertTrue(channelChoosingDropDownEvent.canHandleEvent("choose-bot-channel"));
    }

    @Test
    void canHandleEventWhenComponentIdIsNotForTheEventReturnsFalse() {
        assertFalse(channelChoosingDropDownEvent.canHandleEvent("choose-bot"));
    }

    @Test
    void canHandleEventWhenComponentIdIsNullReturnsFalse() {
        assertFalse(channelChoosingDropDownEvent.canHandleEvent(null));
    }

    @Test
    void canHandleEventWhenComponentIdIsEmptyStringReturnsFalse() {
        assertFalse(channelChoosingDropDownEvent.canHandleEvent(""));
    }
}