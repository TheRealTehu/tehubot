package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class JokeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String jokeText;

    private int numberOfReactions;

    @ManyToOne
    private Guild guild;

    public JokeData() {
    }

    public JokeData(long id, String jokeText, int numberOfReactions, Guild guild) {
        this.id = id;
        this.jokeText = jokeText;
        this.numberOfReactions = numberOfReactions;
        this.guild = guild;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJokeText() {
        return jokeText;
    }

    public void setJokeText(String jokeText) {
        this.jokeText = jokeText;
    }

    public int getNumberOfReactions() {
        return numberOfReactions;
    }

    public void setNumberOfReactions(int numberOfReactions) {
        this.numberOfReactions = numberOfReactions;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }
}