package com.therealtehu.discordbot.TehuBot.database.repository.poll;

import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollAnswerRepository extends JpaRepository<PollAnswerData, Long> {

}
