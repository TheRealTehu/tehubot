package com.therealtehu.discordbot.TehuBot.service.poll;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollAnswerRepository;
import com.therealtehu.discordbot.TehuBot.service.poll.PollAnswerService;
import com.therealtehu.discordbot.TehuBot.service.poll.PollUtil;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PollAnswerServiceTest {

    private PollAnswerService pollAnswerService;
    private final RandomNumberGenerator mockRandomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
    private final PollAnswerRepository mockPollAnswerRepository = Mockito.mock(PollAnswerRepository.class);
    private final SlashCommandInteractionEvent mockSlashCommandInteractionEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final PollData mockPollData = Mockito.mock(PollData.class);
    private final OptionMapping mockOptionMapping = Mockito.mock(OptionMapping.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final MemberData mockMemberData = Mockito.mock(MemberData.class);

    @BeforeEach
    void setup() {
        pollAnswerService = new PollAnswerService(mockRandomNumberGenerator, mockPollAnswerRepository);
    }

    @Test
    void saveAnswersWhenHasOneAnswerAndZeroGuildEmojisSavesAndReturnsOnePollAnswerWithGenericEmoji() {
        when(mockSlashCommandInteractionEvent.getOption(anyString())).thenReturn(mockOptionMapping, (OptionMapping) null);
        when(mockOptionMapping.getAsString()).thenReturn("First answer");
        when(mockSlashCommandInteractionEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getEmojis()).thenReturn(List.of());
        when(mockRandomNumberGenerator.getRandomNumber(anyInt())).thenReturn(0);

        Emoji expectedEmoji = Emoji.fromUnicode(PollUtil.getEmojis().get(0));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(mockPollData);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji(expectedEmoji.getAsReactionCode());

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

        verify(mockPollAnswerRepository).saveAll(expectedAnswerList);
        Assertions.assertEquals(expectedAnswerList, actualAnswerList);
    }

    @Test
    void saveAnswersWhenHasTwoAnswerInSequenceAndZeroGuildEmojisSavesAndReturnsTwoPollAnswerWithGenericEmojis() {
        OptionMapping mockOptionMapping2 = Mockito.mock(OptionMapping.class);
        when(mockSlashCommandInteractionEvent.getOption(anyString())).thenReturn(null);
        when(mockSlashCommandInteractionEvent.getOption("answer1")).thenReturn(mockOptionMapping);
        when(mockSlashCommandInteractionEvent.getOption("answer2")).thenReturn(mockOptionMapping2);

        when(mockOptionMapping.getAsString()).thenReturn("First answer");
        when(mockOptionMapping2.getAsString()).thenReturn("Second answer");

        when(mockSlashCommandInteractionEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getEmojis()).thenReturn(List.of());
        when(mockRandomNumberGenerator.getRandomNumber(anyInt())).thenReturn(0, 1);

        Emoji expectedEmoji = Emoji.fromUnicode(PollUtil.getEmojis().get(0));
        Emoji expectedEmoji2 = Emoji.fromUnicode(PollUtil.getEmojis().get(1));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(mockPollData);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji(expectedEmoji.getAsReactionCode());

        PollAnswerData expectedAnswerData2 = new PollAnswerData();
        expectedAnswerData2.setPollData(mockPollData);
        expectedAnswerData2.setAnswerText("Second answer");
        expectedAnswerData2.setAnswerEmoji(expectedEmoji2.getAsReactionCode());

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData, expectedAnswerData2);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

        verify(mockPollAnswerRepository).saveAll(expectedAnswerList);
        Assertions.assertEquals(actualAnswerList.size(), 2);
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(0)));
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(1)));
    }

    @Test
    void saveAnswersWhenHasTwoAnswerNotInSequenceAndZeroGuildEmojisSavesAndReturnsTwoPollAnswerWithGenericEmojis() {
        OptionMapping mockOptionMapping2 = Mockito.mock(OptionMapping.class);
        when(mockSlashCommandInteractionEvent.getOption(anyString())).thenReturn(null);
        when(mockSlashCommandInteractionEvent.getOption("answer1")).thenReturn(mockOptionMapping);
        when(mockSlashCommandInteractionEvent.getOption("answer15")).thenReturn(mockOptionMapping2);

        when(mockOptionMapping.getAsString()).thenReturn("First answer");
        when(mockOptionMapping2.getAsString()).thenReturn("Fifteenth answer");

        when(mockSlashCommandInteractionEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getEmojis()).thenReturn(List.of());
        when(mockRandomNumberGenerator.getRandomNumber(anyInt())).thenReturn(0, 1);

        Emoji expectedEmoji = Emoji.fromUnicode(PollUtil.getEmojis().get(0));
        Emoji expectedEmoji2 = Emoji.fromUnicode(PollUtil.getEmojis().get(1));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(mockPollData);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji(expectedEmoji.getAsReactionCode());

        PollAnswerData expectedAnswerData2 = new PollAnswerData();
        expectedAnswerData2.setPollData(mockPollData);
        expectedAnswerData2.setAnswerText("Fifteenth answer");
        expectedAnswerData2.setAnswerEmoji(expectedEmoji2.getAsReactionCode());

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData, expectedAnswerData2);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

        verify(mockPollAnswerRepository).saveAll(expectedAnswerList);
        Assertions.assertEquals(actualAnswerList.size(), 2);
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(0)));
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(1)));
    }

    @Test
    void saveAnswersWhenHasOneAnswerAndOneGuildEmojiSavesAndReturnsOnePollAnswerWithGuildEmoji() {
        when(mockSlashCommandInteractionEvent.getOption(anyString())).thenReturn(mockOptionMapping, (OptionMapping) null);
        when(mockOptionMapping.getAsString()).thenReturn("First answer");
        when(mockSlashCommandInteractionEvent.getGuild()).thenReturn(mockGuild);

        RichCustomEmoji mockGuildEmoji = Mockito.mock(RichCustomEmoji.class);
        when(mockGuildEmoji.getAsReactionCode()).thenReturn("000");
        when(mockGuild.getEmojis()).thenReturn(List.of(mockGuildEmoji));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(mockPollData);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji("000");

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

        verify(mockRandomNumberGenerator, times(0)).getRandomNumber(anyInt());
        verify(mockPollAnswerRepository).saveAll(expectedAnswerList);
        Assertions.assertEquals(expectedAnswerList, actualAnswerList);
    }

    @Test
    void saveAnswersWhenHasTwoAnswerInSequenceAndOneGuildEmojiSavesAndReturnsTwoPollAnswerWithOneGuildAndOneGenericEmoji() {
        OptionMapping mockOptionMapping2 = Mockito.mock(OptionMapping.class);
        when(mockSlashCommandInteractionEvent.getOption(anyString())).thenReturn(null);
        when(mockSlashCommandInteractionEvent.getOption("answer1")).thenReturn(mockOptionMapping);
        when(mockSlashCommandInteractionEvent.getOption("answer2")).thenReturn(mockOptionMapping2);

        when(mockOptionMapping.getAsString()).thenReturn("First answer");
        when(mockOptionMapping2.getAsString()).thenReturn("Second answer");

        when(mockSlashCommandInteractionEvent.getGuild()).thenReturn(mockGuild);

        RichCustomEmoji mockGuildEmoji = Mockito.mock(RichCustomEmoji.class);
        when(mockGuildEmoji.getAsReactionCode()).thenReturn("011");
        when(mockGuild.getEmojis()).thenReturn(List.of(mockGuildEmoji));

        when(mockRandomNumberGenerator.getRandomNumber(anyInt())).thenReturn(0);

        Emoji expectedEmoji = Emoji.fromUnicode(PollUtil.getEmojis().get(0));

        PollAnswerData expectedAnswerData = new PollAnswerData();
        expectedAnswerData.setPollData(mockPollData);
        expectedAnswerData.setAnswerText("First answer");
        expectedAnswerData.setAnswerEmoji("011");

        PollAnswerData expectedAnswerData2 = new PollAnswerData();
        expectedAnswerData2.setPollData(mockPollData);
        expectedAnswerData2.setAnswerText("Second answer");
        expectedAnswerData2.setAnswerEmoji(expectedEmoji.getAsReactionCode());

        List<PollAnswerData> expectedAnswerList = List.of(expectedAnswerData, expectedAnswerData2);
        List<PollAnswerData> actualAnswerList = pollAnswerService.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

        verify(mockPollAnswerRepository).saveAll(expectedAnswerList);
        Assertions.assertEquals(actualAnswerList.size(), 2);
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(0)));
        assertTrue(actualAnswerList.contains(expectedAnswerList.get(1)));
    }

    @Test
    void removeVoteWhenVoteIsPresentRemovesVote() {
        PollAnswerData mockPollAnswerData = Mockito.mock(PollAnswerData.class);

        when(mockPollAnswerRepository.findByPollDataAndAnswerEmoji(mockPollData, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));
        when(mockPollAnswerData.removeMember(mockMemberData)).thenReturn(true);

        pollAnswerService.removeVote(mockPollData, mockMemberData, "emoji");

        verify(mockPollAnswerRepository).findByPollDataAndAnswerEmoji(mockPollData, "emoji");
        verify(mockPollAnswerData).removeMember(mockMemberData);
        verify(mockPollAnswerRepository).save(mockPollAnswerData);
    }

    @Test
    void removeVoteWhenPollAnswerDataIsNotPresentDoesNothing() {
        when(mockPollAnswerRepository.findByPollDataAndAnswerEmoji(mockPollData, "emoji"))
                .thenReturn(Optional.empty());
        pollAnswerService.removeVote(mockPollData, mockMemberData, "emoji");

        verify(mockPollAnswerRepository).findByPollDataAndAnswerEmoji(mockPollData, "emoji");
        verifyNoMoreInteractions(mockPollAnswerRepository);
    }

    @Test
    void voteExistsForMemberCallsRepositoryMethod() {
        when(mockPollAnswerRepository.existsByMemberDataAndPollDataAndAnswerEmoji(mockMemberData, mockPollData, "emoji"))
                .thenReturn(true);

        boolean actual = pollAnswerService.voteExistsForMember(mockMemberData, mockPollData, "emoji");

        verify(mockPollAnswerRepository).existsByMemberDataAndPollDataAndAnswerEmoji(mockMemberData, mockPollData, "emoji");
        assertTrue(actual);
    }

    @Test
    void countVotesCallsRepositoryMethod() {
        when(mockPollAnswerRepository.countByPollDataAndMemberData(mockPollData, mockMemberData))
                .thenReturn(1);

        int actual = pollAnswerService.countVotes(mockPollData, mockMemberData);

        verify(mockPollAnswerRepository).countByPollDataAndMemberData(mockPollData, mockMemberData);
        assertEquals(1, actual);
    }

    @Test
    void getPollAnswerDataCallsRepositoryMethod() {
        PollAnswerData mockPollAnswerData = Mockito.mock(PollAnswerData.class);
        when(mockPollAnswerRepository.findByPollDataAndAnswerEmoji(mockPollData, "emoji"))
                .thenReturn(Optional.of(mockPollAnswerData));

        Optional<PollAnswerData> actual = pollAnswerService.getPollAnswerData(mockPollData, "emoji");

        verify(mockPollAnswerRepository).findByPollDataAndAnswerEmoji(mockPollData, "emoji");
        assertEquals(Optional.of(mockPollAnswerData), actual);
    }

    @Test
    void saveAnswerCallsRepositoryMethod() {
        PollAnswerData mockPollAnswerData = Mockito.mock(PollAnswerData.class);

        pollAnswerService.saveAnswer(mockPollAnswerData);

        verify(mockPollAnswerRepository).save(mockPollAnswerData);
    }
}