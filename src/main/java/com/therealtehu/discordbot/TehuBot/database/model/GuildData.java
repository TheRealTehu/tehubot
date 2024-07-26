package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class GuildData {
    @Id
    private Long id;
    private long botChatChannelId;
    public GuildData(){}

    public GuildData(long id, long botChatChannelId) {
        this.id = id;
        this.botChatChannelId = botChatChannelId;
    }
    public long getId() {
        return id;
    }
    public void setId(long guildId) {
        this.id = guildId;
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
        return id == guildData.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
