package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class DiceRollData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private GuildData guild;
    private int numberOfSides;
    private int rolledNumber;

    public DiceRollData() {
    }

    public DiceRollData(long id, GuildData guild, int numberOfSides, int rolledNumber) {
        this.id = id;
        this.guild = guild;
        this.numberOfSides = numberOfSides;
        this.rolledNumber = rolledNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRolledNumber() {
        return rolledNumber;
    }

    public void setRolledNumber(int rolledNumber) {
        this.rolledNumber = rolledNumber;
    }

    public GuildData getGuild() {
        return guild;
    }

    public void setGuild(GuildData guild) {
        this.guild = guild;
    }

    public int getNumberOfSides() {
        return numberOfSides;
    }

    public void setNumberOfSides(int numberOfSides) {
        this.numberOfSides = numberOfSides;
    }
}
