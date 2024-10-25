package com.therealtehu.discordbot.TehuBot.service;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuildFinderTest {
    private GuildFinder guildFinder;
    @Mock
    private ShardManager shardManagerMock;
    @Mock
    private Guild guildMock;

    @BeforeEach
    void setup() {
        guildFinder = new GuildFinder(shardManagerMock);
    }

    @Test
    void findGuildByWhenCorrectGuildIdIsGivenReturnsOptionalOfGuild() {
        when(shardManagerMock.getGuildById(1L)).thenReturn(guildMock);

        Optional<Guild> actual = guildFinder.findGuildBy(1L);

        assertEquals(Optional.of(guildMock), actual);
        verify(shardManagerMock).getGuildById(1L);
    }

    @Test
    void findGuildByWhenIncorrectGuildIdIsGivenReturnsEmptyOptional() {
        when(shardManagerMock.getGuildById(1L)).thenReturn(null);

        Optional<Guild> actual = guildFinder.findGuildBy(1L);

        assertEquals(Optional.empty(), actual);
        verify(shardManagerMock).getGuildById(1L);
    }
}