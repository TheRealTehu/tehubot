package com.therealtehu.discordbot.TehuBot.database.repository.poll;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PollAnswerRepository extends JpaRepository<PollAnswerData, Long> {
    boolean existsByMemberDataAndPollData(MemberData memberData, PollData pollData);
    int countByPollDataAndMemberData(PollData pollData, MemberData memberData);
    Optional<PollAnswerData> findByPollDataAndAnswerEmoji(PollData pollData, String answerEmoji);
    boolean deleteByPollDataAndMemberData(PollData pollData, MemberData memberData);

}
