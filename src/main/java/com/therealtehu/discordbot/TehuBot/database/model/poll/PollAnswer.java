package com.therealtehu.discordbot.TehuBot.database.model.poll;

import com.therealtehu.discordbot.TehuBot.database.model.Member;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class PollAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private PollData pollData;

    private String answerText;

    private String answerEmoji;

    @ManyToMany
    private List<Member> members;

    public PollAnswer() {
    }

    public PollAnswer(long id, PollData pollData, String answerText, String answerEmoji, List<Member> members) {
        this.id = id;
        this.pollData = pollData;
        this.answerText = answerText;
        this.answerEmoji = answerEmoji;
        this.members = members;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PollData getPollData() {
        return pollData;
    }

    public void setPollData(PollData pollData) {
        this.pollData = pollData;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getAnswerEmoji() {
        return answerEmoji;
    }

    public void setAnswerEmoji(String answerEmoji) {
        this.answerEmoji = answerEmoji;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
