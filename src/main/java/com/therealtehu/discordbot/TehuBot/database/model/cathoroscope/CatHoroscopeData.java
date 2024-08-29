package com.therealtehu.discordbot.TehuBot.database.model.cathoroscope;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import com.therealtehu.discordbot.TehuBot.database.model.MemberData;
import jakarta.persistence.*;

@Entity
public class CatHoroscopeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private GuildData guild;

    @ManyToOne
    private MemberData memberData;

    @OneToOne
    private CatBreed catBreed;

    public CatHoroscopeData() {
    }

    public CatHoroscopeData(long id, GuildData guild, MemberData memberData, CatBreed catBreed) {
        this.id = id;
        this.guild = guild;
        this.memberData = memberData;
        this.catBreed = catBreed;
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

    public CatBreed getCatBreed() {
        return catBreed;
    }

    public void setCatBreed(CatBreed catBreed) {
        this.catBreed = catBreed;
    }
}
