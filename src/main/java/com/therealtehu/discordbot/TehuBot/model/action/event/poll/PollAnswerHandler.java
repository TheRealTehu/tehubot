package com.therealtehu.discordbot.TehuBot.model.action.event.poll;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollAnswerData;
import com.therealtehu.discordbot.TehuBot.database.model.poll.PollData;
import com.therealtehu.discordbot.TehuBot.database.repository.poll.PollAnswerRepository;
import com.therealtehu.discordbot.TehuBot.model.action.command.poll.PollUtil;
import com.therealtehu.discordbot.TehuBot.utils.RandomNumberGenerator;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PollAnswerHandler {
    private final RandomNumberGenerator randomNumberGenerator;
    private final PollAnswerRepository pollAnswerRepository;

    @Autowired
    public PollAnswerHandler(RandomNumberGenerator randomNumberGenerator, PollAnswerRepository pollAnswerRepository) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.pollAnswerRepository = pollAnswerRepository;
    }

    public List<PollAnswerData> saveAnswers(SlashCommandInteractionEvent event, PollData pollData) {
        List<String> answerTexts = getAnswerTexts(event);
        List<Emoji> emojis = getEmojisToUse(event.getGuild().getEmojis(), answerTexts.size());

        List<PollAnswerData> answerData = new ArrayList<>();
        for (int i = 0; i < answerTexts.size(); i++) {
            PollAnswerData pollAnswerData = new PollAnswerData();
            pollAnswerData.setPollData(pollData);
            pollAnswerData.setAnswerText(answerTexts.get(i));
            pollAnswerData.setAnswerEmoji(emojis.get(i).getAsReactionCode());

            answerData.add(pollAnswerData);

        }
        pollAnswerRepository.saveAll(answerData);

        return answerData;
    }

    @NotNull
    private static List<String> getAnswerTexts(SlashCommandInteractionEvent event) {
        List<String> answers = new ArrayList<>();
        for (int i = 1; i <= PollUtil.getMaxNumberOfVoteOptions(); i++) {
            OptionMapping answer = event.getOption("answer" + i);
            if (answer != null) {
                answers.add(answer.getAsString());
            }
        }
        return answers;
    }

    private List<Emoji> getEmojisToUse(List<RichCustomEmoji> guildEmojis, int size) {
        List<Emoji> emojisToUse = new ArrayList<>(guildEmojis);
        List<String> unicodeEmojis = PollUtil.getEmojis();
        while (emojisToUse.size() < size) {
            int pick = randomNumberGenerator.getRandomNumber(unicodeEmojis.size());
            Emoji unicodeEmoji = Emoji.fromUnicode(unicodeEmojis.get(pick));
            if (!emojisToUse.contains(unicodeEmoji)) {
                emojisToUse.add(unicodeEmoji);
            }
        }
        Collections.shuffle(guildEmojis);
        return emojisToUse;
    }

    public boolean removeVote(PollData pollData, MemberData memberData) {
        return pollAnswerRepository.deleteByPollDataAndMemberData(pollData, memberData);
    }

    public boolean voteExistsForMember(MemberData memberData, PollData pollData) {
        return pollAnswerRepository.existsByMemberDataAndPollData(memberData, pollData);
    }

    public int countVotes(PollData pollData, MemberData memberData) {
        return pollAnswerRepository.countByPollDataAndMemberData(pollData, memberData);
    }

    @Transactional
    public Optional<PollAnswerData> getPollAnswerData(PollData pollData, String emoji) {
        return pollAnswerRepository.findByPollDataAndAnswerEmoji(pollData, emoji);
    }

    public void saveAnswer(PollAnswerData pollAnswerData) {
        pollAnswerRepository.save(pollAnswerData);
    }
}
