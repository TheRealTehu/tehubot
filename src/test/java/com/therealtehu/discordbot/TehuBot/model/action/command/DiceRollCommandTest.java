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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiceRollCommandTest {
    private DiceRollCommand diceRollCommand;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private RandomNumberGenerator randomNumberGeneratorMock;
    @Mock
    private DiceRollRepository diceRollRepositoryMock;
    @Mock
    private GuildRepository guildRepositoryMock;
    @Mock
    private SlashCommandInteractionEvent eventMock;
    @Mock
    private Member memberMock;
    @Mock
    private Guild guildMock;


    @BeforeEach
    void setup(){
        diceRollCommand = new DiceRollCommand(messageSenderMock, randomNumberGeneratorMock,
                diceRollRepositoryMock, guildRepositoryMock);
    }

    @Test
    void executeCommandWhenNoSidesOptionGivenAndGuildIsInDbRollsBetween1And6AndSavesDataToDb() {
        String expectedMessage = "Member as mention rolled a 6 sided die and the result is: 4!";

        when(eventMock.getOption("sides")).thenReturn(null);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn(("Member as mention"));
        when(randomNumberGeneratorMock.getRandomNumber(1, 6)).thenReturn(4);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        GuildData guildData = new GuildData(1L,1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildData));

        DiceRollData diceRollData = new DiceRollData();
        diceRollData.setGuild(guildData);
        diceRollData.setNumberOfSides(6);
        diceRollData.setRolledNumber(4);

        diceRollCommand.executeCommand(eventMock);

        verify(randomNumberGeneratorMock).getRandomNumber(1,6);
        verify(diceRollRepositoryMock).save(diceRollData);
        verify(messageSenderMock).replyToEvent(eventMock, expectedMessage);
    }

    @Test
    void executeCommandWhenSidesOptionGivenAndGuildIsInDbRollsBetween1AndGivenOptionAndSavesDataToDb() {
        String expectedMessage = "Member as mention rolled a 100 sided die and the result is: 100!";
        OptionMapping mockOption = Mockito.mock(OptionMapping.class);

        when(eventMock.getOption("sides")).thenReturn(mockOption);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn(("Member as mention"));
        when(mockOption.getAsInt()).thenReturn(100);
        when(randomNumberGeneratorMock.getRandomNumber(anyInt(), anyInt())).thenReturn(100);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        GuildData guildData = new GuildData(1L,1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildData));

        DiceRollData diceRollData = new DiceRollData();
        diceRollData.setGuild(guildData);
        diceRollData.setNumberOfSides(100);
        diceRollData.setRolledNumber(100);

        diceRollCommand.executeCommand(eventMock);

        verify(randomNumberGeneratorMock).getRandomNumber(1,100);
        verify(diceRollRepositoryMock).save(diceRollData);
        verify(messageSenderMock).replyToEvent(eventMock, expectedMessage);
    }

    @Test
    void executeCommandWhenSidesOptionGivenAndGuildIsNotInDbReturnsErrorMessage() {
        String expectedMessage = "DATABASE ERROR: Guild not found!";
        OptionMapping mockOption = Mockito.mock(OptionMapping.class);

        when(eventMock.getOption("sides")).thenReturn(mockOption);
        when(mockOption.getAsInt()).thenReturn(100);
        when(randomNumberGeneratorMock.getRandomNumber(anyInt(), anyInt())).thenReturn(100);

        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        diceRollCommand.executeCommand(eventMock);

        verify(randomNumberGeneratorMock).getRandomNumber(1,100);
        verify(diceRollRepositoryMock, times(0)).save(any());
        verify(messageSenderMock).replyToEvent(eventMock, expectedMessage);
    }
}