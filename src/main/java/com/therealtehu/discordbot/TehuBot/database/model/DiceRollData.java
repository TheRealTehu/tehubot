package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class DiceRollData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Guild guild;

    private int rolledNumber;

    public DiceRollData() {
    }

    public DiceRollData(long id, Guild guild, int rolledNumber) {
        this.id = id;
        this.guild = guild;
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

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }
}
