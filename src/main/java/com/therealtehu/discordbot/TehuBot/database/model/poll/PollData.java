package com.therealtehu.discordbot.TehuBot.database.model.poll;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class PollData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String publicId;

    @OneToMany
    private List<PollAnswer> answers;

    @ManyToOne
    private GuildData guild;

    private String pollDescription;

    private LocalDateTime deadLine;

    private boolean isAnonymous;

    private String minimumRole;

    private int numberOfVotesPerMember;

    public PollData() {
    }

    public PollData(long id, String publicId, List<PollAnswer> answers, GuildData guild,
                    String pollDescription, LocalDateTime deadLine, boolean isAnonymous,
                    String minimumRole, int numberOfVotesPerMember) {
        this.id = id;
        this.publicId = publicId;
        this.answers = answers;
        this.guild = guild;
        this.pollDescription = pollDescription;
        this.deadLine = deadLine;
        this.isAnonymous = isAnonymous;
        this.minimumRole = minimumRole;
        this.numberOfVotesPerMember = numberOfVotesPerMember;
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

    public List<PollAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<PollAnswer> answers) {
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

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
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
}
