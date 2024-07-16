package com.therealtehu.discordbot.TehuBot.database.repository;

import com.therealtehu.discordbot.TehuBot.database.model.CoinFlipData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CoinFlipRepository extends JpaRepository<CoinFlipData, Long> {
    Set<CoinFlipData> findAllByGuildId(Long guildId);
}
