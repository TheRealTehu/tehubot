package com.therealtehu.discordbot.TehuBot.database.repository.quiz;

import com.therealtehu.discordbot.TehuBot.database.model.quiz.QuizData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<QuizData, Long> {
    @Query(value = "SELECT q.id FROM quiz_data q WHERE q.guild_id = :guildId ORDER BY q.id DESC LIMIT 1",
            nativeQuery = true)
    Long findLatestIdForGuild(@Param("guildId") long guildId);
    List<QuizData> findAllByGuildId(@Param("guildId") long guildId);
}
