package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.CoinFlipData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.CoinFlipRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinFlipCommandTest {
    private CoinFlipCommand coinFlipCommand;
    @Mock
    private MessageSender messageSenderMock;
    @Mock
    private RandomNumberGenerator randomNumberGeneratorMock;
    @Mock
    private SlashCommandInteractionEvent eventMock;
    @Mock
    private CoinFlipRepository coinFlipRepositoryMock;
    @Mock
    private GuildRepository guildRepositoryMock;
    @Mock
    private Member memberMock;
    @Mock
    private Guild guildMock;
    @Mock
    private GuildData guildDataMock;

    @BeforeEach
    void setup() {
        coinFlipCommand = new CoinFlipCommand(messageSenderMock, randomNumberGeneratorMock,
                coinFlipRepositoryMock, guildRepositoryMock);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndRandomIs0WritesOutHeadAndSavesToDb() {
        String expectedMessage = "Member as mention has flipped a coin and it was HEAD!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, guildDataMock, "Head");

        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);
        when(randomNumberGeneratorMock.getRandomNumber(100)).thenReturn(0);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        coinFlipCommand.executeCommand(eventMock);

        verify(messageSenderMock).reply(eventMock, expectedMessage);
        verify(guildRepositoryMock).findById(1L);
        verify(coinFlipRepositoryMock).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndRandomIsUnder49WritesOutHeadAndSavesToDb() {
        String expectedMessage = "Member as mention has flipped a coin and it was HEAD!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, guildDataMock, "Head");

        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);
        when(randomNumberGeneratorMock.getRandomNumber(100)).thenReturn(10);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        coinFlipCommand.executeCommand(eventMock);

        verify(messageSenderMock).reply(eventMock, expectedMessage);
        verify(guildRepositoryMock).findById(1L);
        verify(coinFlipRepositoryMock).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndRandomIs48WritesOutHeadAndSavesToDb() {
        String expectedMessage = "Member as mention has flipped a coin and it was HEAD!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, guildDataMock, "Head");

        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);
        when(randomNumberGeneratorMock.getRandomNumber(100)).thenReturn(48);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        coinFlipCommand.executeCommand(eventMock);

        verify(messageSenderMock).reply(eventMock, expectedMessage);
        verify(guildRepositoryMock).findById(1L);
        verify(coinFlipRepositoryMock).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndRandomIs49WritesOutTailAndSavesToDb() {
        String expectedMessage = "Member as mention has flipped a coin and it was TAIL!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, guildDataMock, "Tail");

        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);
        when(randomNumberGeneratorMock.getRandomNumber(100)).thenReturn(49);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        coinFlipCommand.executeCommand(eventMock);

        verify(messageSenderMock).reply(eventMock, expectedMessage);
        verify(guildRepositoryMock).findById(1L);
        verify(coinFlipRepositoryMock).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndRandomIsUnder98WritesOutTailAndSavesToDb() {
        String expectedMessage = "Member as mention has flipped a coin and it was TAIL!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, guildDataMock, "Tail");

        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);
        when(randomNumberGeneratorMock.getRandomNumber(100)).thenReturn(67);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        coinFlipCommand.executeCommand(eventMock);

        verify(messageSenderMock).reply(eventMock, expectedMessage);
        verify(guildRepositoryMock).findById(1L);
        verify(coinFlipRepositoryMock).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndRandomIs98WritesOutSideAndSavesToDb() {
        String expectedMessage = "Member as mention has flipped a coin and the coin STOOD ON IT'S SIDE!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, guildDataMock, "Side");

        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);
        when(randomNumberGeneratorMock.getRandomNumber(100)).thenReturn(98);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        coinFlipCommand.executeCommand(eventMock);

        verify(messageSenderMock).reply(eventMock, expectedMessage);
        verify(guildRepositoryMock).findById(1L);
        verify(coinFlipRepositoryMock).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndRandomIs99WritesOutLostAndSavesToDb() {
        String expectedMessage = "Member as mention has flipped a coin and the coin bounced behind the couch, you can't find it!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, guildDataMock, "Lost");

        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);
        when(randomNumberGeneratorMock.getRandomNumber(100)).thenReturn(99);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        coinFlipCommand.executeCommand(eventMock);

        verify(messageSenderMock).reply(eventMock, expectedMessage);
        verify(guildRepositoryMock).findById(1L);
        verify(coinFlipRepositoryMock).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenMemberHasPermissionAndRandomIsOver99WritesOutLostAndSavesToDb() {
        String expectedMessage = "Member as mention has flipped a coin and the coin bounced behind the couch, you can't find it!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, guildDataMock, "Lost");

        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);
        when(randomNumberGeneratorMock.getRandomNumber(100)).thenReturn(110);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.of(guildDataMock));

        coinFlipCommand.executeCommand(eventMock);

        verify(messageSenderMock).reply(eventMock, expectedMessage);
        verify(guildRepositoryMock).findById(1L);
        verify(coinFlipRepositoryMock).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenMemberHasPermissionButGuildIsNotInDatabaseWritesOutErrorMessageAndDoesNotSaveToDb() {
        String expectedMessage = "DATABASE ERROR: Guild not found!";

        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(true);
        when(randomNumberGeneratorMock.getRandomNumber(100)).thenReturn(0);
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.getAsMention()).thenReturn("Member as mention");
        when(eventMock.getGuild()).thenReturn(guildMock);
        when(guildMock.getIdLong()).thenReturn(1L);
        when(guildRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        coinFlipCommand.executeCommand(eventMock);

        verify(messageSenderMock).reply(eventMock, expectedMessage);
        verify(guildRepositoryMock).findById(1L);
        verify(coinFlipRepositoryMock, times(0)).save(any());
    }

    @Test
    void executeCommandWhenMemberDoesNotHavePermissionDoesNothing() {
        when(eventMock.getMember()).thenReturn(memberMock);
        when(memberMock.hasPermission(Permission.MESSAGE_SEND)).thenReturn(false);

        coinFlipCommand.executeCommand(eventMock);

        verifyNoInteractions(messageSenderMock);
        verifyNoInteractions(guildRepositoryMock);
        verifyNoInteractions(randomNumberGeneratorMock);
    }
}