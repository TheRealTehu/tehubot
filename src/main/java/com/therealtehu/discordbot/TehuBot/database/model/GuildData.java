package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class GuildData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private long guildId;
    private long botChatChannelId;
    public GuildData(){}

    public GuildData(long id, long guildId, long botChatChannelId) {
        this.id = id;
        this.guildId = guildId;
        this.botChatChannelId = botChatChannelId;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getGuildId() {
        return guildId;
    }
    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }
    public long getBotChatChannelId() {
        return botChatChannelId;
    }
    public void setBotChatChannelId(long botChatChannelId) {
        this.botChatChannelId = botChatChannelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuildData guildData = (GuildData) o;
        return id == guildData.id && guildId == guildData.guildId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guildId);
    }
}
