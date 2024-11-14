package com.therealtehu.discordbot.TehuBot.service.poll;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollAnswerRepository;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollAnswerServiceTest {
    private PollAnswerService pollAnswerService;
    @Mock
    private RandomNumberGenerator randomNumberGeneratorMock;
    @Mock
    private PollAnswerRepository pollAnswerRepositoryMock;
    @Mock
    private SlashCommandInteractionEvent slashCommandInteractionEventMock;
    @Mock
    private PollData pollDataMock;
    @Mock
    private OptionMapping optionMappingMock;
    @Mock
    private Guild guildMock;
    @Mock
    private MemberData memberDataMock;
    @Mock
    private PollAnswerData pollAnswerDataMock;

    @BeforeEach
    void setup() {
        pollAnswerService = new PollAnswerService(randomNumberGeneratorMock, pollAnswerRepositoryMock);
    }

    @Test
    void saveAnswersWhenHasOneAnswerAndZeroGuildEmojisSavesAndReturnsOnePollAnswerWithGenericEmoji() {
        when(slashCommandInteractionEventMock.getOption(anyString())).thenReturn(optionMappingMock, (OptionMapping) null);
        when(optionMappingMock.getAsString()).thenReturn("First answer");
        when(slashCommandInteractionEventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getEmojis()).thenReturn(List.of());
        when(randomNumberGeneratorMock.getRandomNumber(anyInt())).thenReturn(0);

        Emoji expectedEmoji = Emoji.fromUnicode(PollUtil.getEmojis().get(0));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(pollDataMock);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji(expectedEmoji.getAsReactionCode());

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(slashCommandInteractionEventMock, pollDataMock);

        verify(pollAnswerRepositoryMock).saveAll(expectedAnswerList);
        Assertions.assertEquals(expectedAnswerList, actualAnswerList);
    }

    @Test
    void saveAnswersWhenHasTwoAnswerInSequenceAndZeroGuildEmojisSavesAndReturnsTwoPollAnswerWithGenericEmojis() {
        OptionMapping mockOptionMapping2 = Mockito.mock(OptionMapping.class);
        when(slashCommandInteractionEventMock.getOption(anyString())).thenReturn(null);
        when(slashCommandInteractionEventMock.getOption("answer1")).thenReturn(optionMappingMock);
        when(slashCommandInteractionEventMock.getOption("answer2")).thenReturn(mockOptionMapping2);

        when(optionMappingMock.getAsString()).thenReturn("First answer");
        when(mockOptionMapping2.getAsString()).thenReturn("Second answer");

        when(slashCommandInteractionEventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getEmojis()).thenReturn(List.of());
        when(randomNumberGeneratorMock.getRandomNumber(anyInt())).thenReturn(0, 1);

        Emoji expectedEmoji = Emoji.fromUnicode(PollUtil.getEmojis().get(0));
        Emoji expectedEmoji2 = Emoji.fromUnicode(PollUtil.getEmojis().get(1));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(pollDataMock);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji(expectedEmoji.getAsReactionCode());

        PollAnswerData expectedAnswerData2 = new PollAnswerData();
        expectedAnswerData2.setPollData(pollDataMock);
        expectedAnswerData2.setAnswerText("Second answer");
        expectedAnswerData2.setAnswerEmoji(expectedEmoji2.getAsReactionCode());

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData, expectedAnswerData2);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(slashCommandInteractionEventMock, pollDataMock);

        verify(pollAnswerRepositoryMock).saveAll(expectedAnswerList);
        Assertions.assertEquals(actualAnswerList.size(), 2);
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(0)));
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(1)));
    }

    @Test
    void saveAnswersWhenHasTwoAnswerNotInSequenceAndZeroGuildEmojisSavesAndReturnsTwoPollAnswerWithGenericEmojis() {
        OptionMapping mockOptionMapping2 = Mockito.mock(OptionMapping.class);
        when(slashCommandInteractionEventMock.getOption(anyString())).thenReturn(null);
        when(slashCommandInteractionEventMock.getOption("answer1")).thenReturn(optionMappingMock);
        when(slashCommandInteractionEventMock.getOption("answer15")).thenReturn(mockOptionMapping2);

        when(optionMappingMock.getAsString()).thenReturn("First answer");
        when(mockOptionMapping2.getAsString()).thenReturn("Fifteenth answer");

        when(slashCommandInteractionEventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getEmojis()).thenReturn(List.of());
        when(randomNumberGeneratorMock.getRandomNumber(anyInt())).thenReturn(0, 1);

        Emoji expectedEmoji = Emoji.fromUnicode(PollUtil.getEmojis().get(0));
        Emoji expectedEmoji2 = Emoji.fromUnicode(PollUtil.getEmojis().get(1));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(pollDataMock);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji(expectedEmoji.getAsReactionCode());

        PollAnswerData expectedAnswerData2 = new PollAnswerData();
        expectedAnswerData2.setPollData(pollDataMock);
        expectedAnswerData2.setAnswerText("Fifteenth answer");
        expectedAnswerData2.setAnswerEmoji(expectedEmoji2.getAsReactionCode());

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData, expectedAnswerData2);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(slashCommandInteractionEventMock, pollDataMock);

        verify(pollAnswerRepositoryMock).saveAll(expectedAnswerList);
        Assertions.assertEquals(actualAnswerList.size(), 2);
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(0)));
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(1)));
    }

    @Test
    void saveAnswersWhenHasOneAnswerAndOneGuildEmojiSavesAndReturnsOnePollAnswerWithGuildEmoji() {
        when(slashCommandInteractionEventMock.getOption(anyString())).thenReturn(optionMappingMock, (OptionMapping) null);
        when(optionMappingMock.getAsString()).thenReturn("First answer");
        when(slashCommandInteractionEventMock.getGuild()).thenReturn(guildMock);

        RichCustomEmoji mockGuildEmoji = Mockito.mock(RichCustomEmoji.class);
        when(mockGuildEmoji.getAsReactionCode()).thenReturn("000");
        when(guildMock.getEmojis()).thenReturn(List.of(mockGuildEmoji));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(pollDataMock);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji("000");

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(slashCommandInteractionEventMock, pollDataMock);

        verify(randomNumberGeneratorMock, times(0)).getRandomNumber(anyInt());
        verify(pollAnswerRepositoryMock).saveAll(expectedAnswerList);
        Assertions.assertEquals(expectedAnswerList, actualAnswerList);
    }

    @Test
    void saveAnswersWhenHasTwoAnswerInSequenceAndOneGuildEmojiSavesAndReturnsTwoPollAnswerWithOneGuildAndOneGenericEmoji() {
        OptionMapping mockOptionMapping2 = Mockito.mock(OptionMapping.class);
        when(slashCommandInteractionEventMock.getOption(anyString())).thenReturn(null);
        when(slashCommandInteractionEventMock.getOption("answer1")).thenReturn(optionMappingMock);
        when(slashCommandInteractionEventMock.getOption("answer2")).thenReturn(mockOptionMapping2);

        when(optionMappingMock.getAsString()).thenReturn("First answer");
        when(mockOptionMapping2.getAsString()).thenReturn("Second answer");

        when(slashCommandInteractionEventMock.getGuild()).thenReturn(guildMock);

        RichCustomEmoji mockGuildEmoji = Mockito.mock(RichCustomEmoji.class);
        when(mockGuildEmoji.getAsReactionCode()).thenReturn("011");
        when(guildMock.getEmojis()).thenReturn(List.of(mockGuildEmoji));

        when(randomNumberGeneratorMock.getRandomNumber(anyInt())).thenReturn(0);

        Emoji expectedEmoji = Emoji.fromUnicode(PollUtil.getEmojis().get(0));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(pollDataMock);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji("011");

        PollAnswerData expectedAnswerData2 = new PollAnswerData();
        expectedAnswerData2.setPollData(pollDataMock);
        expectedAnswerData2.setAnswerText("Second answer");
        expectedAnswerData2.setAnswerEmoji(expectedEmoji.getAsReactionCode());

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData, expectedAnswerData2);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(slashCommandInteractionEventMock, pollDataMock);

        verify(pollAnswerRepositoryMock).saveAll(expectedAnswerList);
        Assertions.assertEquals(actualAnswerList.size(), 2);
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(0)));
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(1)));
    }

    @Test
    void removeVoteWhenVoteIsPresentRemovesVote() {
        PollAnswerData mockPollAnswerData = Mockito.mock(PollAnswerData.class);

        when(pollAnswerRepositoryMock.findByPollDataAndAnswerEmoji(pollDataMock, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));
        when(mockPollAnswerData.removeMember(memberDataMock)).thenReturn(true);

        pollAnswerService.removeVote(pollDataMock, memberDataMock, "emoji");

        verify(pollAnswerRepositoryMock).findByPollDataAndAnswerEmoji(pollDataMock, "emoji");
        verify(mockPollAnswerData).removeMember(memberDataMock);
        verify(pollAnswerRepositoryMock).save(mockPollAnswerData);
    }

    @Test
    void removeVoteWhenPollAnswerDataIsNotPresentDoesNothing() {
        when(pollAnswerRepositoryMock.findByPollDataAndAnswerEmoji(pollDataMock, "emoji"))
                .thenReturn(Optional.empty());
        pollAnswerService.removeVote(pollDataMock, memberDataMock, "emoji");

        verify(pollAnswerRepositoryMock).findByPollDataAndAnswerEmoji(pollDataMock, "emoji");
        verifyNoMoreInteractions(pollAnswerRepositoryMock);
    }

    @Test
    void voteExistsForMemberCallsRepositoryMethod() {
        when(pollAnswerRepositoryMock.existsByMemberDataAndPollDataAndAnswerEmoji(memberDataMock, pollDataMock, "emoji"))
                .thenReturn(true);

        boolean actual = pollAnswerService.voteExistsForMember(memberDataMock, pollDataMock, "emoji");

        verify(pollAnswerRepositoryMock).existsByMemberDataAndPollDataAndAnswerEmoji(memberDataMock, pollDataMock, "emoji");
        assertTrue(actual);
    }

    @Test
    void countVotesCallsRepositoryMethod() {
        when(pollAnswerRepositoryMock.countByPollDataAndMemberData(pollDataMock, memberDataMock))
                .thenReturn(1);

        int actual = pollAnswerService.countVotes(pollDataMock, memberDataMock);

        verify(pollAnswerRepositoryMock).countByPollDataAndMemberData(pollDataMock, memberDataMock);
        assertEquals(1, actual);
    }

    @Test
    void addVoteWhenPollAnswerDataIsPresentAddsMemberToAnswerSavesChangeAndReturnsTrue() {
        when(pollAnswerRepositoryMock.findByPollDataAndAnswerEmoji(pollDataMock, "emoji"))
                .thenReturn(Optional.of(pollAnswerDataMock));

        boolean actual = pollAnswerService.addVote(pollDataMock, "emoji", memberDataMock);

        verify(pollAnswerDataMock).addMember(memberDataMock);
        verify(pollAnswerRepositoryMock).save(pollAnswerDataMock);
        assertTrue(actual);
    }

    @Test
    void addVoteWhenPollAnswerDataIsNotPresentSavesNothingAndReturnsFalse() {
        when(pollAnswerRepositoryMock.findByPollDataAndAnswerEmoji(pollDataMock, "emoji"))
                .thenReturn(Optional.empty());

        boolean actual = pollAnswerService.addVote(pollDataMock, "emoji", memberDataMock);

        verifyNoInteractions(pollAnswerDataMock);
        verify(pollAnswerRepositoryMock).findByPollDataAndAnswerEmoji(pollDataMock, "emoji");
        verifyNoMoreInteractions(pollAnswerRepositoryMock);
        assertFalse(actual);
    }
}