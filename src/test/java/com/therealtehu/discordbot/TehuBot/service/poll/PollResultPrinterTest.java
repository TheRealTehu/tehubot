package com.therealtehu.discordbot.TehuBot.service.poll;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.service.GuildFinder;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.PriorityQueue;

import static org.mockito.Mockito.*;

class PollResultPrinterTest {
    private PollResultPrinter pollResultPrinter;
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final GuildFinder mockGuildFinder = Mockito.mock(GuildFinder.class);
    private final PollData mockPollData = Mockito.mock(PollData.class);
    private final PollAnswerData mockPollAnswerData = Mockito.mock(PollAnswerData.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final GuildData mockGuildData = Mockito.mock(GuildData.class);
    private final TextChannel mockTextChannel = Mockito.mock(TextChannel.class);

    @BeforeEach
    void setup() {
        pollResultPrinter = new PollResultPrinter(mockMessageSender, mockGuildFinder);
    }

    @Test
    void printResultWhenGuildIsNotFoundPrintsNothing() {
        when(mockPollData.getPublicId()).thenReturn("pollId");
        when(mockPollData.getNumberOfVotes()).thenReturn(1);
        when(mockPollData.getPollDescription()).thenReturn("The description of the poll");
        when(mockPollAnswerData.getNumberOfVotes()).thenReturn(1);
        PriorityQueue<PollAnswerData> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(mockPollAnswerData);
        when(mockPollData.getAnswersInOrder()).thenReturn(priorityQueue);
        when(mockPollAnswerData.getAnswerText()).thenReturn("Text of the answer");

        when(mockPollData.getGuild()).thenReturn(mockGuildData);
        when(mockGuildData.getId()).thenReturn(1L);
        when(mockGuildFinder.findGuildBy(1L)).thenReturn(Optional.empty());

        pollResultPrinter.printResult(mockPollData);

        verify(mockGuildFinder).findGuildBy(1L);
        verifyNoInteractions(mockMessageSender);
    }

    @Test
    void printResultWhenOneAnswerIsPresentWithOneVotePrintsCorrectResult() {
        when(mockPollData.getPublicId()).thenReturn("pollId");
        when(mockPollData.getNumberOfVotes()).thenReturn(1);
        when(mockPollData.getPollDescription()).thenReturn("The description of the poll");
        when(mockPollAnswerData.getNumberOfVotes()).thenReturn(1);
        PriorityQueue<PollAnswerData> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(mockPollAnswerData);
        when(mockPollData.getAnswersInOrder()).thenReturn(priorityQueue);
        when(mockPollAnswerData.getAnswerText()).thenReturn("Text of the answer");

        when(mockPollData.getGuild()).thenReturn(mockGuildData);
        when(mockGuildData.getId()).thenReturn(1L);
        when(mockGuildFinder.findGuildBy(1L)).thenReturn(Optional.of(mockGuild));
        when(mockGuildData.getBotChatChannelId()).thenReturn(1L);
        when(mockGuild.getTextChannelById(1L)).thenReturn(mockTextChannel);

        String expectedText = "Poll: pollId has closed with *1* votes!\n__The description of the poll__\n"
                + "Text of the answer: 1 (100.0%)\n";

        pollResultPrinter.printResult(mockPollData);

        verify(mockGuildFinder).findGuildBy(1L);
        verify(mockMessageSender).sendMessage(mockTextChannel, expectedText);
    }

    @Test
    void printResultWhenTwoAnswersArePresentPrintsCorrectResult() {
        when(mockPollData.getPublicId()).thenReturn("pollId");
        when(mockPollData.getNumberOfVotes()).thenReturn(1);
        when(mockPollData.getPollDescription()).thenReturn("The description of the poll");
        when(mockPollAnswerData.getNumberOfVotes()).thenReturn(1);

        PollAnswerData mockPollAnswerData2 = Mockito.mock(PollAnswerData.class);
        when(mockPollAnswerData2.getNumberOfVotes()).thenReturn(0);

        PriorityQueue<PollAnswerData> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(mockPollAnswerData);
        priorityQueue.add(mockPollAnswerData2);
        when(mockPollData.getAnswersInOrder()).thenReturn(priorityQueue);
        when(mockPollAnswerData.getAnswerText()).thenReturn("Text of first answer");
        when(mockPollAnswerData2.getAnswerText()).thenReturn("Text of second answer");

        when(mockPollData.getGuild()).thenReturn(mockGuildData);
        when(mockGuildData.getId()).thenReturn(1L);
        when(mockGuildFinder.findGuildBy(1L)).thenReturn(Optional.of(mockGuild));
        when(mockGuildData.getBotChatChannelId()).thenReturn(1L);
        when(mockGuild.getTextChannelById(1L)).thenReturn(mockTextChannel);

        String expectedText = "Poll: pollId has closed with *1* votes!\n__The description of the poll__\n"
                + "Text of first answer: 1 (100.0%)\n"
                + "Text of second answer: 0 (0.0%)\n";

        pollResultPrinter.printResult(mockPollData);

        verify(mockGuildFinder).findGuildBy(1L);
        verify(mockMessageSender).sendMessage(mockTextChannel, expectedText);
    }
}