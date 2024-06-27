package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class SteamRouletteData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Guild guild;

    @ManyToOne
    private Member member;

    private String gameTitle;

    public SteamRouletteData() {
    }

    public SteamRouletteData(long id, Guild guild, Member member, String gameTitle) {
        this.id = id;
        this.guild = guild;
        this.member = member;
        this.gameTitle = gameTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }
}
