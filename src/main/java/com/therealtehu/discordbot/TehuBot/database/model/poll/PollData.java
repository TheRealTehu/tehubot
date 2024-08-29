package com.therealtehu.discordbot.TehuBot.database.model.poll;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class PollData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String publicId;
    @OneToMany
    private List<PollAnswerData> answers;
    @ManyToOne
    private GuildData guild;
    private String pollDescription;
    private OffsetDateTime deadLine;
    private boolean isAnonymous;
    private String minimumRole;
    private int numberOfVotesPerMember;

    private boolean isClosed;

    public PollData() {
    }

    public PollData(long id, String publicId, List<PollAnswerData> answers, GuildData guild,
                    String pollDescription, OffsetDateTime deadLine, boolean isAnonymous,
                    String minimumRole, int numberOfVotesPerMember, boolean isClosed) {
        this.id = id;
        this.publicId = publicId;
        this.answers = answers;
        this.guild = guild;
        this.pollDescription = pollDescription;
        this.deadLine = deadLine;
        this.isAnonymous = isAnonymous;
        this.minimumRole = minimumRole;
        this.numberOfVotesPerMember = numberOfVotesPerMember;
        this.isClosed = isClosed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public List<PollAnswerData> getAnswers() {
        return answers;
    }

    public void setAnswers(List<PollAnswerData> answers) {
        this.answers = answers;
    }

    public GuildData getGuild() {
        return guild;
    }

    public void setGuild(GuildData guild) {
        this.guild = guild;
    }

    public String getPollDescription() {
        return pollDescription;
    }

    public void setPollDescription(String pollDescription) {
        this.pollDescription = pollDescription;
    }

    public OffsetDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(OffsetDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getMinimumRole() {
        return minimumRole;
    }

    public void setMinimumRole(String minimumRole) {
        this.minimumRole = minimumRole;
    }

    public int getNumberOfVotesPerMember() {
        return numberOfVotesPerMember;
    }

    public void setNumberOfVotesPerMember(int numberOfVotesPerMember) {
        this.numberOfVotesPerMember = numberOfVotesPerMember;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PollData pollData = (PollData) o;
        return id == pollData.id && Objects.equals(publicId, pollData.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicId);
    }

    @Override
    public String toString() {
        return "PollData{" +
                "id=" + id +
                ", publicId='" + publicId + '\'' +
                ", answers=" + answers +
                ", guild=" + guild +
                ", pollDescription='" + pollDescription + '\'' +
                ", deadLine=" + deadLine +
                ", isAnonymous=" + isAnonymous +
                ", minimumRole='" + minimumRole + '\'' +
                ", numberOfVotesPerMember=" + numberOfVotesPerMember +
                ", isClosed=" + isClosed +
                '}';
    }
}
