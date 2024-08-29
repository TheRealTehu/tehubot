package com.therealtehu.discordbot.TehuBot.model.action.command.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollAnswerRepository;
import com.therealtehu.discordbot.TehuBot.model.action.event.poll.PollAnswerHandler;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PollAnswerHandlerTest {

    private PollAnswerHandler pollAnswerHandler;
    private final RandomNumberGenerator mockRandomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
    private final PollAnswerRepository mockPollAnswerRepository = Mockito.mock(PollAnswerRepository.class);
    private final SlashCommandInteractionEvent mockSlashCommandInteractionEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final PollData mockPollData = Mockito.mock(PollData.class);
    private final OptionMapping mockOptionMapping = Mockito.mock(OptionMapping.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);

    @BeforeEach
    void setup() {
        pollAnswerHandler = new PollAnswerHandler(mockRandomNumberGenerator, mockPollAnswerRepository);
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
        List<PollAnswerData> actualAnswerList = pollAnswerHandler.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

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
        List<PollAnswerData> actualAnswerList = pollAnswerHandler.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

        verify(mockPollAnswerRepository).saveAll(expectedAnswerList);
        Assertions.assertEquals(actualAnswerList.size(), 2);
        Assertions.assertTrue(actualAnswerList.contains(expectedAnswerList.get(0)));
        Assertions.assertTrue(actualAnswerList.contains(expectedAnswerList.get(1)));
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
        List<PollAnswerData> actualAnswerList = pollAnswerHandler.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

        verify(mockPollAnswerRepository).saveAll(expectedAnswerList);
        Assertions.assertEquals(actualAnswerList.size(), 2);
        Assertions.assertTrue(actualAnswerList.contains(expectedAnswerList.get(0)));
        Assertions.assertTrue(actualAnswerList.contains(expectedAnswerList.get(1)));
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
        List<PollAnswerData> actualAnswerList = pollAnswerHandler.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

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
        List<PollAnswerData> actualAnswerList = pollAnswerHandler.saveAnswers(mockSlashCommandInteractionEvent, mockPollData);

        verify(mockPollAnswerRepository).saveAll(expectedAnswerList);
        Assertions.assertEquals(actualAnswerList.size(), 2);
        Assertions.assertTrue(actualAnswerList.contains(expectedAnswerList.get(0)));
        Assertions.assertTrue(actualAnswerList.contains(expectedAnswerList.get(1)));
    }
}