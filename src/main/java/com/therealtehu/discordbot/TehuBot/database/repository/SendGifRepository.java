package com.therealtehu.discordbot.TehuBot.database.repository;

import com.therealtehu.discordbot.TehuBot.database.model.SendGifData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendGifRepository extends JpaRepository<SendGifData, Long> {
}
