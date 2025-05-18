package com.therealtehu.discordbot.TehuBot.database.repository.quiz;

import com.therealtehu.discordbot.TehuBot.database.model.quiz.QuizData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<QuizData, Long> {
    Long findLatestIdForGuild(@Param("guildId") long guildId);
}
