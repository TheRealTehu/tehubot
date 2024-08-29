package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class SteamRouletteData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private GuildData guild;

    @ManyToOne
    private MemberData memberData;

    private String gameTitle;

    public SteamRouletteData() {
    }

    public SteamRouletteData(long id, GuildData guild, MemberData memberData, String gameTitle) {
        this.id = id;
        this.guild = guild;
        this.memberData = memberData;
        this.gameTitle = gameTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GuildData getGuild() {
        return guild;
    }

    public void setGuild(GuildData guild) {
        this.guild = guild;
    }

    public MemberData getMember() {
        return memberData;
    }

    public void setMember(MemberData memberData) {
        this.memberData = memberData;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }
}
