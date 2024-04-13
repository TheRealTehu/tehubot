package com.therealtehu.discordbot.TehuBot.model.command;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiceRollCommandTest {

    private DiceRollCommand diceRollCommand;

    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final RandomNumberGenerator mockRandomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
    private final SlashCommandInteractionEvent mockCommandEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final Member mockMember = Mockito.mock(Member.class);

    @BeforeEach
    void setup(){
        diceRollCommand = new DiceRollCommand(mockMessageSender, mockRandomNumberGenerator);
    }

    @Test
    void executeCommandWhenNoSidesOptionGivenRollsBetween1And6() {
        String expectedMessage = "Member as mention rolled a 6 sided die and the result is: 4!";

        when(mockCommandEvent.getOption("sides")).thenReturn(null);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn(("Member as mention"));
        when(mockRandomNumberGenerator.getRandomNumber(anyInt(), anyInt())).thenReturn(4);

        diceRollCommand.executeCommand(mockCommandEvent);

        verify(mockRandomNumberGenerator).getRandomNumber(1,6);
        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenSidesOptionGivenRollsBetween1AndGivenOption() {
        String expectedMessage = "Member as mention rolled a 100 sided die and the result is: 100!";
        OptionMapping mockOption = Mockito.mock(OptionMapping.class);

        when(mockCommandEvent.getOption("sides")).thenReturn(mockOption);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn(("Member as mention"));
        when(mockOption.getAsInt()).thenReturn(100);
        when(mockRandomNumberGenerator.getRandomNumber(anyInt(), anyInt())).thenReturn(100);

        diceRollCommand.executeCommand(mockCommandEvent);

        verify(mockRandomNumberGenerator).getRandomNumber(1,100);
        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }
}