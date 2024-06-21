package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CoinFlipData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String flippedSide;

    public CoinFlipData() {
    }

    public CoinFlipData(long id, String flippedSide) {
        this.id = id;
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
}
