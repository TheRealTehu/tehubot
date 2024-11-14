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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.PriorityQueue;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollResultPrinterTest {
    private PollResultPrinter pollResultPrinter;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private GuildFinder guildFinderMock;
    @Mock
    private PollData pollDataMock;
    @Mock
    private PollAnswerData pollAnswerDataMock;
    @Mock
    private Guild guildMock;
    @Mock
    private GuildData guildDataMock;
    @Mock
    private TextChannel textChannelMock;

    @BeforeEach
    void setup() {
        pollResultPrinter = new PollResultPrinter(messageSenderMock, guildFinderMock);
    }

    @Test
    void printResultWhenGuildIsNotFoundPrintsNothing() {
        when(pollDataMock.getPublicId()).thenReturn("pollId");
        when(pollDataMock.getNumberOfVotes()).thenReturn(1);
        when(pollDataMock.getPollDescription()).thenReturn("The description of the poll");
        when(pollAnswerDataMock.getNumberOfVotes()).thenReturn(1);
        PriorityQueue<PollAnswerData> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(pollAnswerDataMock);
        when(pollDataMock.getAnswersInOrder()).thenReturn(priorityQueue);
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Text of the answer");

        when(pollDataMock.getGuild()).thenReturn(guildDataMock);
        when(guildDataMock.getId()).thenReturn(1L);
        when(guildFinderMock.findGuildBy(1L)).thenReturn(Optional.empty());

        pollResultPrinter.printResult(pollDataMock);

        verify(guildFinderMock).findGuildBy(1L);
        verifyNoInteractions(messageSenderMock);
    }

    @Test
    void printResultWhenOneAnswerIsPresentWithOneVotePrintsCorrectResult() {
        when(pollDataMock.getPublicId()).thenReturn("pollId");
        when(pollDataMock.getNumberOfVotes()).thenReturn(1);
        when(pollDataMock.getPollDescription()).thenReturn("The description of the poll");
        when(pollAnswerDataMock.getNumberOfVotes()).thenReturn(1);
        PriorityQueue<PollAnswerData> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(pollAnswerDataMock);
        when(pollDataMock.getAnswersInOrder()).thenReturn(priorityQueue);
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Text of the answer");

        when(pollDataMock.getGuild()).thenReturn(guildDataMock);
        when(guildDataMock.getId()).thenReturn(1L);
        when(guildFinderMock.findGuildBy(1L)).thenReturn(Optional.of(guildMock));
        when(guildDataMock.getBotChatChannelId()).thenReturn(1L);
        when(guildMock.getTextChannelById(1L)).thenReturn(textChannelMock);

        String expectedText = "Poll: pollId has closed with *1* votes!\n__The description of the poll__\n"
                + "Text of the answer: 1 (100.0%)\n";

        pollResultPrinter.printResult(pollDataMock);

        verify(guildFinderMock).findGuildBy(1L);
        verify(messageSenderMock).sendMessage(textChannelMock, expectedText);
    }

    @Test
    void printResultWhenTwoAnswersArePresentPrintsCorrectResult() {
        when(pollDataMock.getPublicId()).thenReturn("pollId");
        when(pollDataMock.getNumberOfVotes()).thenReturn(1);
        when(pollDataMock.getPollDescription()).thenReturn("The description of the poll");
        when(pollAnswerDataMock.getNumberOfVotes()).thenReturn(1);

        PollAnswerData mockPollAnswerData2 = Mockito.mock(PollAnswerData.class);
        when(mockPollAnswerData2.getNumberOfVotes()).thenReturn(0);

        PriorityQueue<PollAnswerData> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(pollAnswerDataMock);
        priorityQueue.add(mockPollAnswerData2);
        when(pollDataMock.getAnswersInOrder()).thenReturn(priorityQueue);
        when(pollAnswerDataMock.getAnswerText()).thenReturn("Text of first answer");
        when(mockPollAnswerData2.getAnswerText()).thenReturn("Text of second answer");

        when(pollDataMock.getGuild()).thenReturn(guildDataMock);
        when(guildDataMock.getId()).thenReturn(1L);
        when(guildFinderMock.findGuildBy(1L)).thenReturn(Optional.of(guildMock));
        when(guildDataMock.getBotChatChannelId()).thenReturn(1L);
        when(guildMock.getTextChannelById(1L)).thenReturn(textChannelMock);

        String expectedText = "Poll: pollId has closed with *1* votes!\n__The description of the poll__\n"
                + "Text of first answer: 1 (100.0%)\n"
                + "Text of second answer: 0 (0.0%)\n";

        pollResultPrinter.printResult(pollDataMock);

        verify(guildFinderMock).findGuildBy(1L);
        verify(messageSenderMock).sendMessage(textChannelMock, expectedText);
    }
}