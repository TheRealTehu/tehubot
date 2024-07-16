package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class GuildData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private long guildId;
    private long defaultTextChannelId;
    private long wikiChannelId;
    private long pollChannelId;
    private long gameChannelId;
    private long hallOfFameChannelId;
    private long catHoroscopeChannelId;
    private long jokeChannelId;
    private long botChatChannelId;
    private long steamRouletteChannelId;

    public GuildData(){}

    public GuildData(long id, long guildId, long defaultTextChannelId, long wikiChannelId, long pollChannelId,
                     long gameChannelId, long hallOfFameChannelId, long catHoroscopeChannelId, long jokeChannelId,
                     long botChatChannelId, long steamRouletteChannelId) {
        this.id = id;
        this.guildId = guildId;
        this.defaultTextChannelId = defaultTextChannelId;
        this.wikiChannelId = wikiChannelId;
        this.pollChannelId = pollChannelId;
        this.gameChannelId = gameChannelId;
        this.hallOfFameChannelId = hallOfFameChannelId;
        this.catHoroscopeChannelId = catHoroscopeChannelId;
        this.jokeChannelId = jokeChannelId;
        this.botChatChannelId = botChatChannelId;
        this.steamRouletteChannelId = steamRouletteChannelId;
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

    public long getDefaultTextChannelId() {
        return defaultTextChannelId;
    }

    public void setDefaultTextChannelId(long defaultTextChannelId) {
        this.defaultTextChannelId = defaultTextChannelId;
    }

    public long getWikiChannelId() {
        return wikiChannelId;
    }

    public void setWikiChannelId(long wikiChannelId) {
        this.wikiChannelId = wikiChannelId;
    }

    public long getPollChannelId() {
        return pollChannelId;
    }

    public void setPollChannelId(long pollChannelId) {
        this.pollChannelId = pollChannelId;
    }

    public long getGameChannelId() {
        return gameChannelId;
    }

    public void setGameChannelId(long gameChannelId) {
        this.gameChannelId = gameChannelId;
    }

    public long getHallOfFameChannelId() {
        return hallOfFameChannelId;
    }

    public void setHallOfFameChannelId(long hallOfFameChannelId) {
        this.hallOfFameChannelId = hallOfFameChannelId;
    }

    public long getCatHoroscopeChannelId() {
        return catHoroscopeChannelId;
    }

    public void setCatHoroscopeChannelId(long catHoroscopeChannelId) {
        this.catHoroscopeChannelId = catHoroscopeChannelId;
    }

    public long getJokeChannelId() {
        return jokeChannelId;
    }

    public void setJokeChannelId(long jokeChannelId) {
        this.jokeChannelId = jokeChannelId;
    }

    public long getBotChatChannelId() {
        return botChatChannelId;
    }

    public void setBotChatChannelId(long botChatChannelId) {
        this.botChatChannelId = botChatChannelId;
    }

    public long getSteamRouletteChannelId() {
        return steamRouletteChannelId;
    }

    public void setSteamRouletteChannelId(long steamRouletteChannelId) {
        this.steamRouletteChannelId = steamRouletteChannelId;
    }
}
