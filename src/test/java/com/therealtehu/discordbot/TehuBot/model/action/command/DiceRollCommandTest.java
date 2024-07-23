package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.DiceRollData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.DiceRollRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class DiceRollCommandTest {
    private DiceRollCommand diceRollCommand;
    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final RandomNumberGenerator mockRandomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
    private final DiceRollRepository mockDiceRollRepository = Mockito.mock(DiceRollRepository.class);
    private final GuildRepository mockGuildRepository = Mockito.mock(GuildRepository.class);
    private final SlashCommandInteractionEvent mockCommandEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final Member mockMember = Mockito.mock(Member.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);

    @BeforeEach
    void setup(){
        diceRollCommand = new DiceRollCommand(mockMessageSender, mockRandomNumberGenerator,
                mockDiceRollRepository, mockGuildRepository);
    }

    @Test
    void executeCommandWhenNoSidesOptionGivenAndGuildIsInDbRollsBetween1And6AndSavesDataToDb() {
        String expectedMessage = "Member as mention rolled a 6 sided die and the result is: 4!";

        when(mockCommandEvent.getOption("sides")).thenReturn(null);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn(("Member as mention"));
        when(mockRandomNumberGenerator.getRandomNumber(1, 6)).thenReturn(4);

        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        GuildData guildData = new GuildData(1L,1L,1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(Optional.of(guildData));

        DiceRollData diceRollData = new DiceRollData();
        diceRollData.setGuild(guildData);
        diceRollData.setNumberOfSides(6);
        diceRollData.setRolledNumber(4);

        diceRollCommand.executeCommand(mockCommandEvent);

        verify(mockRandomNumberGenerator).getRandomNumber(1,6);
        verify(mockDiceRollRepository).save(diceRollData);
        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenSidesOptionGivenAndGuildIsInDbRollsBetween1AndGivenOptionAndSavesDataToDb() {
        String expectedMessage = "Member as mention rolled a 100 sided die and the result is: 100!";
        OptionMapping mockOption = Mockito.mock(OptionMapping.class);

        when(mockCommandEvent.getOption("sides")).thenReturn(mockOption);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn(("Member as mention"));
        when(mockOption.getAsInt()).thenReturn(100);
        when(mockRandomNumberGenerator.getRandomNumber(anyInt(), anyInt())).thenReturn(100);

        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        GuildData guildData = new GuildData(1L,1L,1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(Optional.of(guildData));

        DiceRollData diceRollData = new DiceRollData();
        diceRollData.setGuild(guildData);
        diceRollData.setNumberOfSides(100);
        diceRollData.setRolledNumber(100);

        diceRollCommand.executeCommand(mockCommandEvent);

        verify(mockRandomNumberGenerator).getRandomNumber(1,100);
        verify(mockDiceRollRepository).save(diceRollData);
        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenSidesOptionGivenAndGuildIsNotInDbReturnsErrorMessage() {
        String expectedMessage = "DATABASE ERROR: Guild not found!";
        OptionMapping mockOption = Mockito.mock(OptionMapping.class);

        when(mockCommandEvent.getOption("sides")).thenReturn(mockOption);
        when(mockOption.getAsInt()).thenReturn(100);
        when(mockRandomNumberGenerator.getRandomNumber(anyInt(), anyInt())).thenReturn(100);

        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(Optional.empty());

        diceRollCommand.executeCommand(mockCommandEvent);

        verify(mockRandomNumberGenerator).getRandomNumber(1,100);
        verify(mockDiceRollRepository, times(0)).save(any());
        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }
}