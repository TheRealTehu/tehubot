package com.therealtehu.discordbot.TehuBot.database.model.cathoroscope;

import com.therealtehu.discordbot.TehuBot.database.model.Guild;
import com.therealtehu.discordbot.TehuBot.database.model.Member;
import jakarta.persistence.*;

@Entity
public class CatHoroscopeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Guild guild;

    @OneToOne
    private Member member;

    @OneToOne
    private CatBreed catBreed;

    public CatHoroscopeData() {
    }

    public CatHoroscopeData(long id, Guild guild, Member member, CatBreed catBreed) {
        this.id = id;
        this.guild = guild;
        this.member = member;
        this.catBreed = catBreed;
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

    public CatBreed getCatBreed() {
        return catBreed;
    }

    public void setCatBreed(CatBreed catBreed) {
        this.catBreed = catBreed;
    }
}
