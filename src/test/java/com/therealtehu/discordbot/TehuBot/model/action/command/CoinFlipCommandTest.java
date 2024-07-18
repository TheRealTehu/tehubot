package com.therealtehu.discordbot.TehuBot.model.action.command;

import com.therealtehu.discordbot.TehuBot.database.model.CoinFlipData;
import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.repository.CoinFlipRepository;
import com.therealtehu.discordbot.TehuBot.database.repository.GuildRepository;
import com.therealtehu.discordbot.TehuBot.service.display.MessageSender;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CoinFlipCommandTest {

    private CoinFlipCommand coinFlipCommand;

    private final MessageSender mockMessageSender = Mockito.mock(MessageSender.class);
    private final RandomNumberGenerator mockRandomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
    private final SlashCommandInteractionEvent mockCommandEvent = Mockito.mock(SlashCommandInteractionEvent.class);
    private final CoinFlipRepository mockCoinFlipRepository = Mockito.mock(CoinFlipRepository.class);
    private final GuildRepository mockGuildRepository = Mockito.mock(GuildRepository.class);
    private final Member mockMember = Mockito.mock(Member.class);
    private final Guild mockGuild = Mockito.mock(Guild.class);
    private final GuildData mockGuildData = Mockito.mock(GuildData.class);

    @BeforeEach
    void setup() {
        coinFlipCommand = new CoinFlipCommand(mockMessageSender, mockRandomNumberGenerator,
                mockCoinFlipRepository, mockGuildRepository);
    }

    @Test
    void executeCommandWhenRandomIs0ShouldWriteOutHead() {
        String expectedMessage = "Member as mention has flipped a coin and it was HEAD!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, mockGuildData, "Head");

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(0);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(mockGuildData);

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
        verify(mockGuildRepository).findByGuildId(1L);
        verify(mockCoinFlipRepository).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenRandomIsUnder49ShouldWriteOutHead() {
        String expectedMessage = "Member as mention has flipped a coin and it was HEAD!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, mockGuildData, "Head");

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(10);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(mockGuildData);

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
        verify(mockGuildRepository).findByGuildId(1L);
        verify(mockCoinFlipRepository).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenRandomIs48ShouldWriteOutHead() {
        String expectedMessage = "Member as mention has flipped a coin and it was HEAD!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, mockGuildData, "Head");

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(48);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(mockGuildData);

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
        verify(mockGuildRepository).findByGuildId(1L);
        verify(mockCoinFlipRepository).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenRandomIs49ShouldWriteOutTail() {
        String expectedMessage = "Member as mention has flipped a coin and it was TAIL!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, mockGuildData, "Tail");

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(49);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(mockGuildData);

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
        verify(mockGuildRepository).findByGuildId(1L);
        verify(mockCoinFlipRepository).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenRandomIsUnder98ShouldWriteOutTail() {
        String expectedMessage = "Member as mention has flipped a coin and it was TAIL!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, mockGuildData, "Tail");


        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(67);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(mockGuildData);

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
        verify(mockGuildRepository).findByGuildId(1L);
        verify(mockCoinFlipRepository).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenRandomIs98ShouldWriteOutSide() {
        String expectedMessage = "Member as mention has flipped a coin and the coin STOOD ON IT'S SIDE!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, mockGuildData, "Side");

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(98);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(mockGuildData);

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
        verify(mockGuildRepository).findByGuildId(1L);
        verify(mockCoinFlipRepository).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenRandomIs99ShouldWriteOutLost() {
        String expectedMessage = "Member as mention has flipped a coin and the coin bounced behind the couch, you can't find it!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, mockGuildData, "Lost");

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(99);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(mockGuildData);

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
        verify(mockGuildRepository).findByGuildId(1L);
        verify(mockCoinFlipRepository).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenRandomIsOver99ShouldWriteOutLost() {
        String expectedMessage = "Member as mention has flipped a coin and the coin bounced behind the couch, you can't find it!";
        CoinFlipData expectedCoinFlipData = new CoinFlipData(0L, mockGuildData, "Lost");

        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(110);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(mockGuildData);

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
        verify(mockGuildRepository).findByGuildId(1L);
        verify(mockCoinFlipRepository).save(expectedCoinFlipData);
    }

    @Test
    void executeCommandWhenGuildIsNotInDatabaseShouldWriteOutErrorMessage() {
        String expectedMessage = "DATABASE ERROR: Guild not found!";
        when(mockRandomNumberGenerator.getRandomNumber(100)).thenReturn(0);
        when(mockCommandEvent.getMember()).thenReturn(mockMember);
        when(mockMember.getAsMention()).thenReturn("Member as mention");
        when(mockCommandEvent.getGuild()).thenReturn(mockGuild);
        when(mockGuild.getIdLong()).thenReturn(1L);
        when(mockGuildRepository.findByGuildId(1L)).thenReturn(null);

        coinFlipCommand.executeCommand(mockCommandEvent);

        verify(mockMessageSender).replyToEvent(mockCommandEvent, expectedMessage);
        verify(mockGuildRepository).findByGuildId(1L);
        verify(mockCoinFlipRepository, times(0)).save(any());
    }
}