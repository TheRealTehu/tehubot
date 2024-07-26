package com.therealtehu.discordbot.TehuBot.database.repository;

import com.therealtehu.discordbot.TehuBot.database.model.GuildStatisticsData;

import java.util.Optional;

public interface GuildStatisticsRepository extends ViewRepository<GuildStatisticsData, Long>{
    Optional<GuildStatisticsData> findByGuildId(Long id);
}
