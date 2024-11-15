package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class GetWikiData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GuildData guild;

    private String searchedTopic;

    public GetWikiData() {
    }

    public GetWikiData(Long id, GuildData guild, String searchedTopic) {
        this.id = id;
        this.guild = guild;
        this.searchedTopic = searchedTopic;
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

    public void setGuild(GuildData guildData) {
        this.guild = guildData;
    }

    public String getSearchedTopic() {
        return searchedTopic;
    }

    public void setSearchedTopic(String searchedTopic) {
        this.searchedTopic = searchedTopic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetWikiData that = (GetWikiData) o;
        return Objects.equals(id, that.id) && Objects.equals(guild, that.guild);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guild);
    }
}
