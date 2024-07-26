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
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ChannelChoosingDropDownEventTest {
    private ChannelChoosingDropDownEvent channelChoosingDropDownEvent;
    private final GuildRepository mockGuildRepository = Mockito.mock(GuildRepository.class);
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final EntitySelectInteractionEvent mockEntitySelectInteractionEvent = Mockito.mock(EntitySelectInteractionEvent.class);
    private final Member mockMember = Mockito.mock(Member.class);
    private final Mentions mockMentions = Mockito.mock(Mentions.class);
    private final GuildChannel mockGuildChannel = Mockito.mock(GuildChannel.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
    private final ReplyCallbackAction mockReplyCallbackAction = Mockito.mock(ReplyCallbackAction.class);
    private final RestAction<Void> mockRestAction = Mockito.mock(RestAction.class);

    @BeforeEach
    void setup() {
        channelChoosingDropDownEvent = new ChannelChoosingDropDownEvent(mockMessageSender, mockGuildRepository);
    }

    @Test
    void handleEventWhenComponentIdIsForTheEventAndUserIsAdminAndGuildIsInDbUpdatesDbAndRepliesToEventAndDeletesEventMessage() {
        when(mockEntitySelectInteractionEvent.getComponentId()).thenReturn("choose-bot-channel");
        when(mockEntitySelectInteractionEvent.getMember()).thenReturn(mockMember);
        when(mockMember.hasPermission(Permission.ADMINISTRATOR)).thenReturn(true);

        when(mockEntitySelectInteractionEvent.getMentions()).thenReturn(mockMentions);
        when(mockMentions.getChannels()).thenReturn(List.of(mockGuildChannel));

        when(mockEntitySelectInteractionEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        GuildData actualGuildData = new GuildData();
        actualGuildData.setId(1L);
        when(mockGuildRepository.findById(1L)).thenReturn(Optional.of(actualGuildData));
        when(mockGuildChannel.getIdLong()).thenReturn(100L);

        GuildData expectedGuildData = new GuildData();
        expectedGuildData.setId(1L);
        expectedGuildData.setBotChatChannelId(100L);

        when(mockEntitySelectInteractionEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockEntitySelectInteractionEvent.getMessageIdLong()).thenReturn(30L);
        when(mockEntitySelectInteractionEvent.reply("Setup finished!")).thenReturn(mockReplyCallbackAction);
        when(mockReplyCallbackAction.and(any())).thenReturn(mockRestAction);

        channelChoosingDropDownEvent.handle(mockEntitySelectInteractionEvent);

        verify(mockGuildRepository).save(expectedGuildData);
        verify(mockEntitySelectInteractionEvent).reply("Setup finished!");
        verify(mockReplyCallbackAction).and(mockMessageChannelUnion.deleteMessageById(30L));
        verify(mockRestAction).queue();
    }

    @Test
    void handleEventWhenComponentIdIsForTheEventAndUserIsAdminAndGuildIsNotInDbRepliesToEventWithErrorMessageAndDeletesEventMessage() {
        when(mockEntitySelectInteractionEvent.getComponentId()).thenReturn("choose-bot-channel");
        when(mockEntitySelectInteractionEvent.getMember()).thenReturn(mockMember);
        when(mockMember.hasPermission(Permission.ADMINISTRATOR)).thenReturn(true);

        when(mockEntitySelectInteractionEvent.getMentions()).thenReturn(mockMentions);
        when(mockMentions.getChannels()).thenReturn(List.of(mockGuildChannel));

        when(mockEntitySelectInteractionEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findById(1L)).thenReturn(Optional.empty());

        when(mockEntitySelectInteractionEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockEntitySelectInteractionEvent.getMessageIdLong()).thenReturn(30L);
        when(mockEntitySelectInteractionEvent.reply("Guild not found in database!")).thenReturn(mockReplyCallbackAction);
        when(mockReplyCallbackAction.and(any())).thenReturn(mockRestAction);

        channelChoosingDropDownEvent.handle(mockEntitySelectInteractionEvent);

        verify(mockGuildRepository, times(0)).save(any());
        verify(mockEntitySelectInteractionEvent).reply("Guild not found in database!");
        verify(mockReplyCallbackAction).and(mockMessageChannelUnion.deleteMessageById(30L));
        verify(mockRestAction).queue();
    }

    @Test
    void handleEventWhenComponentIdIsForTheEventAndUserIsNotAdminRepliesToEventWithErrorMessage() {
        when(mockEntitySelectInteractionEvent.getComponentId()).thenReturn("choose-bot-channel");
        when(mockEntitySelectInteractionEvent.getMember()).thenReturn(mockMember);
        when(mockMember.hasPermission(Permission.ADMINISTRATOR)).thenReturn(false);

        when(mockEntitySelectInteractionEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockEntitySelectInteractionEvent.getMessageIdLong()).thenReturn(30L);
        when(mockEntitySelectInteractionEvent.reply("Only admins can do the setup!")).thenReturn(mockReplyCallbackAction);

        channelChoosingDropDownEvent.handle(mockEntitySelectInteractionEvent);

        verify(mockGuildRepository, times(0)).save(any());
        verify(mockEntitySelectInteractionEvent).reply("Only admins can do the setup!");
        verify(mockReplyCallbackAction, times(0)).and(mockMessageChannelUnion.deleteMessageById(30L));
        verify(mockReplyCallbackAction).queue();
    }

    @Test
    void handleEventWhenComponentIdIsNotForTheEventNothingHappens() {
        when(mockEntitySelectInteractionEvent.getComponentId()).thenReturn("not-choose-bot-channel-id");

        channelChoosingDropDownEvent.handle(mockEntitySelectInteractionEvent);

        verifyNoInteractions(mockGuildRepository);
        verify(mockEntitySelectInteractionEvent, times(0)).reply(anyString());
        verifyNoInteractions(mockReplyCallbackAction);
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