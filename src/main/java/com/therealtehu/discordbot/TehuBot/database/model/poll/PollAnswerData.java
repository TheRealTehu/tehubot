package com.therealtehu.discordbot.TehuBot.database.model.poll;

import com.therealtehu.discordbot.TehuBot.database.model.Member;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class PollAnswerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private PollData pollData;

    private String answerText;

    private String answerEmoji;

    @ManyToMany
    private List<Member> members;

    public PollAnswerData() {
    }

    public PollAnswerData(long id, PollData pollData, String answerText, String answerEmoji, List<Member> members) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PollAnswerData that = (PollAnswerData) o;
        return id == that.id && Objects.equals(pollData, that.pollData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pollData);
    }

    @Override
    public String toString() {
        return "PollAnswerData{" +
                "id=" + id +
                ", pollData public id= " + pollData.getPublicId() +
                ", answerText='" + answerText + '\'' +
                ", answerEmoji='" + answerEmoji + '\'' +
                ", members=" + members +
                '}';
    }
}