package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class CoinFlipData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private GuildData guild;

    private String flippedSide;

    public CoinFlipData() {
    }

    public CoinFlipData(long id, GuildData guild, String flippedSide) {
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

    public GuildData getGuild() {
        return guild;
    }

    public void setGuild(GuildData guild) {
        this.guild = guild;
    }
}
