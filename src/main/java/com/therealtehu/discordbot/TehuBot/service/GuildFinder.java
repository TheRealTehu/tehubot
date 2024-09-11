package com.therealtehu.discordbot.TehuBot.service;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuildFinder {
    private final ShardManager shardManager;

    @Autowired
    public GuildFinder(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    public Optional<Guild> findGuildBy(long guildId) {
        Guild guild = shardManager.getGuildById(guildId);
        if (guild != null) {
            return Optional.of(guild);
        }
        return Optional.empty();
    }
}
