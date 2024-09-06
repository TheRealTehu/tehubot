package com.therealtehu.discordbot.TehuBot.listeners.event;

import com.therealtehu.discordbot.TehuBot.model.action.event.EventHandler;
import com.therealtehu.discordbot.TehuBot.model.action.event.EventName;
import com.therealtehu.discordbot.TehuBot.model.action.event.guild.server_join.ChannelChoosingDropDownEvent;
import com.therealtehu.discordbot.TehuBot.model.action.event.poll.PollVoteEvent;
import com.therealtehu.discordbot.TehuBot.service.display.Display;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.requests.RestAction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class EventListenerTest {
    private EventListener eventListener;
    private final EventHandler mockEventHandler = Mockito.mock(EventHandler.class);
    private final Display mockMessageSender = Mockito.mock(Display.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final DefaultGuildChannelUnion mockDefaultChannel = Mockito.mock(DefaultGuildChannelUnion.class);
    private final MessageChannelUnion mockMessageChannelUnion = Mockito.mock(MessageChannelUnion.class);
    private final TextChannel mockTextChannel = Mockito.mock(TextChannel.class);
    private final Message mockMessage = Mockito.mock(Message.class);
    private final RestAction<Message> mockRestAction = Mockito.mock(RestAction.class);

    @Test
    void onGuildJoinWhenEventHandlerListIsEmptyLogsError() {
        eventListener = new EventListener(List.of(), mockMessageSender);
        GuildJoinEvent mockJoinEvent = Mockito.mock(GuildJoinEvent.class);

        when(mockJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockDefaultChannel);
        when(mockDefaultChannel.asTextChannel()).thenReturn(mockTextChannel);

        eventListener.onGuildJoin(mockJoinEvent);

        verify(mockMessageSender).sendMessage(mockTextChannel,
                "No suitable EventHandler for: " + EventName.SERVER_JOIN.getEventName());
    }

    @Test
    void onGuildJoinWhenHasOneEventHandlerButNotTheNeededEventHandlerLogsError() {
        eventListener = new EventListener(List.of(mockEventHandler), mockMessageSender);
        GuildJoinEvent mockJoinEvent = Mockito.mock(GuildJoinEvent.class);

        when(mockEventHandler.getName()).thenReturn("otherhandler");
        when(mockJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockDefaultChannel);
        when(mockDefaultChannel.asTextChannel()).thenReturn(mockTextChannel);

        eventListener.onGuildJoin(mockJoinEvent);

        verify(mockMessageSender).sendMessage(mockTextChannel,
                "No suitable EventHandler for: " + EventName.SERVER_JOIN.getEventName());
    }

    @Test
    void onGuildJoinWhenHasOneEventHandlerAndItIsTheNeededEventHandlerExecutesEvent() {
        eventListener = new EventListener(List.of(mockEventHandler), mockMessageSender);
        GuildJoinEvent mockJoinEvent = Mockito.mock(GuildJoinEvent.class);

        when(mockEventHandler.getName()).thenReturn(EventName.SERVER_JOIN.getEventName());

        when(mockJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockDefaultChannel);
        when(mockDefaultChannel.asTextChannel()).thenReturn(mockTextChannel);

        eventListener.onGuildJoin(mockJoinEvent);

        verify(mockEventHandler).handle(mockJoinEvent);
    }

    @Test
    void onGuildJoinWhenHasTwoEventHandlerAndOneIsTheNeededEventHandlerExecutesEvent() {
        EventHandler otherMockEventHandler = Mockito.mock(EventHandler.class);
        eventListener = new EventListener(List.of(mockEventHandler, otherMockEventHandler), mockMessageSender);
        GuildJoinEvent mockJoinEvent = Mockito.mock(GuildJoinEvent.class);

        when(mockEventHandler.getName()).thenReturn(EventName.SERVER_JOIN.getEventName());
        when(otherMockEventHandler.getName()).thenReturn("otherhandler");

        when(mockJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockDefaultChannel);
        when(mockDefaultChannel.asTextChannel()).thenReturn(mockTextChannel);

        eventListener.onGuildJoin(mockJoinEvent);

        verify(mockEventHandler).handle(mockJoinEvent);
    }
    @Test
    void onGuildMemberJoinWhenEventHandlerListIsEmptyLogsError() {
        eventListener = new EventListener(List.of(), mockMessageSender);
        GuildMemberJoinEvent mockJoinEvent = Mockito.mock(GuildMemberJoinEvent.class);
        when(mockJoinEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getDefaultChannel()).thenReturn(mockDefaultChannel);
        when(mockDefaultChannel.asTextChannel()).thenReturn(mockTextChannel);

        eventListener.onGuildMemberJoin(mockJoinEvent);

        verify(mockMessageSender).sendMessage(mockTextChannel,
                "No suitable EventHandler for: " + EventName.SERVER_NEW_MEMBER.getEventName());
    }

    @Test
    void onEntitySelectInteractionWhenHasCorrectEventHandlerAndCorrectDropDownEventExecutesEvent() {
        ChannelChoosingDropDownEvent mockDropdownEventHandler = Mockito.mock(ChannelChoosingDropDownEvent.class);
        eventListener = new EventListener(List.of(mockDropdownEventHandler), mockMessageSender);
        String componentId = "1";

        EntitySelectInteractionEvent mockEntitySelectInteractionEvent = Mockito.mock(EntitySelectInteractionEvent.class);

        when(mockEntitySelectInteractionEvent.getComponentId()).thenReturn(componentId);
        when(mockDropdownEventHandler.canHandleEvent(componentId)).thenReturn(true);
        when(mockEntitySelectInteractionEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        eventListener.onEntitySelectInteraction(mockEntitySelectInteractionEvent);

        verify(mockDropdownEventHandler).handle(mockEntitySelectInteractionEvent);
    }

    @Test
    void onMessageReactionAddWhenMessageIsNotAPollIsIgnored() {
        MessageReactionAddEvent mockMessageReactionAddEvent = Mockito.mock(MessageReactionAddEvent.class);
        PollVoteEvent mockPollVoteEvent = Mockito.mock(PollVoteEvent.class);

        eventListener = new EventListener(List.of(mockPollVoteEvent), mockMessageSender);

        when(mockMessageReactionAddEvent.retrieveMessage()).thenReturn(mockRestAction);

        doAnswer(ans -> {
            Consumer<Message> callback = (Consumer<Message>) ans.getArgument(0);
            callback.accept(mockMessage);
            return null;
        }).when(mockRestAction).queue(any(Consumer.class));

        when(mockMessage.getContentRaw()).thenReturn("Not a poll");

        eventListener.onMessageReactionAdd(mockMessageReactionAddEvent);

        verifyNoInteractions(mockPollVoteEvent);
    }

    @Test
    void onMessageReactionAddWhenMessageIsAPollThePollHandlerHandlesIt() {
        MessageReactionAddEvent mockMessageReactionAddEvent = Mockito.mock(MessageReactionAddEvent.class);
        PollVoteEvent mockPollVoteEvent = Mockito.mock(PollVoteEvent.class);

        eventListener = new EventListener(List.of(mockPollVoteEvent), mockMessageSender);

        when(mockMessageReactionAddEvent.retrieveMessage()).thenReturn(mockRestAction);
        when(mockMessageReactionAddEvent.getChannel()).thenReturn(mockMessageChannelUnion);
        when(mockMessageChannelUnion.asTextChannel()).thenReturn(mockTextChannel);

        MessageReaction mockMessageReaction = Mockito.mock(MessageReaction.class);
        when(mockMessageReactionAddEvent.getReaction()).thenReturn(mockMessageReaction);
        when(mockMessageReaction.getMessageIdLong()).thenReturn(1L);

        doAnswer(ans -> {
            Consumer<Message> callback = (Consumer<Message>) ans.getArgument(0);
            callback.accept(mockMessage);
            return null;
        }).when(mockRestAction).queue(any(Consumer.class));

        when(mockPollVoteEvent.getName()).thenReturn(EventName.POLL_VOTE.getEventName());
        when(mockMessage.getContentRaw()).thenReturn("__poll id:__");

        eventListener.onMessageReactionAdd(mockMessageReactionAddEvent);

        verify(mockPollVoteEvent).handle(any());
    }
}