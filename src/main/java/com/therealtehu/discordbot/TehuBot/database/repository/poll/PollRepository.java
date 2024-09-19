package com.therealtehu.discordbot.TehuBot.database.repository.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<PollData, Long> {
    @Query(value = "SELECT p.id FROM poll_data p WHERE p.guild_id = :guildId ORDER BY p.id DESC LIMIT 1",
            nativeQuery = true)
    Long findLatestIdForGuild(@Param("guildId") long guildId);

    Optional<PollData> findByPublicId(String publicId);

    List<PollData> findByIsClosedFalseAndDeadLineIsNotNullAndDeadLineBefore(OffsetDateTime currentTime);
}
