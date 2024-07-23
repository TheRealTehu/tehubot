package com.therealtehu.discordbot.TehuBot.database.repository;

import com.therealtehu.discordbot.TehuBot.database.model.DiceRollData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DiceRollRepository extends JpaRepository<DiceRollData, Long> {
    Set<DiceRollData> findAllByGuildId(Long guildId);
}
