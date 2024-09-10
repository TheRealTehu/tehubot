package com.therealtehu.discordbot.TehuBot.database.model.poll;

import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class PollAnswerData implements Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private PollData pollData;

    private String answerText;

    private String answerEmoji;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<MemberData> memberData;

    public PollAnswerData() {
        memberData = new ArrayList<>();
    }

    public PollAnswerData(long id, PollData pollData, String answerText, String answerEmoji, List<MemberData> memberData) {
        this.id = id;
        this.pollData = pollData;
        this.answerText = answerText;
        this.answerEmoji = answerEmoji;
        this.memberData = memberData;
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

    public List<MemberData> getMembers() {
        return memberData;
    }

    public void setMembers(List<MemberData> memberData) {
        this.memberData = memberData;
    }

    public void addMember(MemberData memberData) {
        this.memberData.add(memberData);
    }

    public int getNumberOfVotes() {
        return memberData.size();
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
                ", members=" + memberData +
                '}';
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if(o instanceof PollAnswerData other) {
            int thisSize = this.memberData != null ? this.memberData.size() : 0;
            int otherSize = other.memberData != null ? other.memberData.size() : 0;
            return Integer.compare(otherSize, thisSize);
        }
        return 1;
    }
}
