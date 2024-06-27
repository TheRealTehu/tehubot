package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class CoinFlipData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Guild guild;

    private String flippedSide;

    public CoinFlipData() {
    }

    public CoinFlipData(long id, Guild guild, String flippedSide) {
        this.id = id;
        this.guild = guild;
        this.flippedSide = flippedSide;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlippedSide() {
        return flippedSide;
    }

    public void setFlippedSide(String flippedSide) {
        this.flippedSide = flippedSide;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }
}
