package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class HallOfFameData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private GuildData guild;

    @ManyToOne
    private MemberData memberData;

    private long channelId;

    private String massageLink;

    private int numberOfEmojis;

    public HallOfFameData() {
    }

    public HallOfFameData(long id, GuildData guild, MemberData memberData, long channelId, String massageLink, int numberOfEmojis) {
        this.id = id;
        this.guild = guild;
        this.memberData = memberData;
        this.channelId = channelId;
        this.massageLink = massageLink;
        this.numberOfEmojis = numberOfEmojis;
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

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getMassageLink() {
        return massageLink;
    }

    public void setMassageLink(String massageLink) {
        this.massageLink = massageLink;
    }

    public int getNumberOfEmojis() {
        return numberOfEmojis;
    }

    public void setNumberOfEmojis(int numberOfEmojis) {
        this.numberOfEmojis = numberOfEmojis;
    }
}
