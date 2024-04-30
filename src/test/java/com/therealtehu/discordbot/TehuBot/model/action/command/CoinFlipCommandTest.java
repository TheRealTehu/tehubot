package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CoinFlipCommandTest {

    private CoinFlipCommand coinFlipCommand;

    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final RandomNumberGenerator mockRandomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
    private final SlashCommandInteractionEvent mockCommandEvent = Mockito.mock(SlashCommandInteractionEvent.class);

    private final Member mockMember = Mockito.mock(Member.class);

    @BeforeEach
    void setup() {
        coinFlipCommand = new CoinFlipCommand(mockMessageSender, mockRandomNumberGenerator);
    }

    @Test
    void executeCommandWhenRandomIs0ShouldWriteOutHead() {
        String expectedMessage = "Member as mention has flipped a coin and it was HEAD!";

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(0);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenRandomIsUnder49ShouldWriteOutHead() {
        String expectedMessage = "Member as mention has flipped a coin and it was HEAD!";

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(10);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenRandomIs48ShouldWriteOutHead() {
        String expectedMessage = "Member as mention has flipped a coin and it was HEAD!";

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(48);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenRandomIs49ShouldWriteOutTail() {
        String expectedMessage = "Member as mention has flipped a coin and it was TAIL!";

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(49);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenRandomIsUnder98ShouldWriteOutTail() {
        String expectedMessage = "Member as mention has flipped a coin and it was TAIL!";

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(67);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenRandomIs98ShouldWriteOutSide() {
        String expectedMessage = "Member as mention has flipped a coin and the coin STOOD ON IT'S SIDE!";

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(98);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenRandomIs99ShouldWriteOutLost() {
        String expectedMessage = "Member as mention has flipped a coin and the coin bounced behind the couch, you can't find it!";

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(99);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }

    @Test
    void executeCommandWhenRandomIsOver99ShouldWriteOutLost() {
        String expectedMessage = "Member as mention has flipped a coin and the coin bounced behind the couch, you can't find it!";

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(110);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
    }
}