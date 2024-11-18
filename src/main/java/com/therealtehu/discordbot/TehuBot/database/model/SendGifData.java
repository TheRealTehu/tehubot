package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

@Entity
public class SendGifData {
    @Id
    private Long id;
    @ManyToOne
    private GuildData guild;
    private String searchTerm;

    public SendGifData() {
    }

    public SendGifData(Long id, GuildData guild, String searchTerm) {
        this.id = id;
        this.guild = guild;
        this.searchTerm = searchTerm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public GuildData getGuild() {
        return guild;
    }

    public void setGuild(GuildData guild) {
        this.guild = guild;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendGifData that = (SendGifData) o;
        return Objects.equals(id, that.id) && Objects.equals(guild, that.guild);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guild);
    }
}
