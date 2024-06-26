package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class GuildStatisticsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Guild guild;

    private int numberOfWikisChecked;

    private int numberOfPolls;

    private int numberOfGifsSent;

    private int numberOfDiceRolls;

    private int numberOfCoinFlips;

    private int numberOfCatHoroscopes;

    private int numberOfSteamRouletteSpins;

    private int numberOfSongsListenedTo;

    private int numberOfVideosPlayed;

    public GuildStatisticsData() {
    }

    public GuildStatisticsData(long id, Guild guild, int numberOfWikisChecked, int numberOfPolls,
                               int numberOfGifsSent, int numberOfDiceRolls, int numberOfCoinFlips,
                               int numberOfCatHoroscopes, int numberOfSteamRouletteSpins,
                               int numberOfSongsListenedTo, int numberOfVideosPlayed) {
        this.id = id;
        this.guild = guild;
        this.numberOfWikisChecked = numberOfWikisChecked;
        this.numberOfPolls = numberOfPolls;
        this.numberOfGifsSent = numberOfGifsSent;
        this.numberOfDiceRolls = numberOfDiceRolls;
        this.numberOfCoinFlips = numberOfCoinFlips;
        this.numberOfCatHoroscopes = numberOfCatHoroscopes;
        this.numberOfSteamRouletteSpins = numberOfSteamRouletteSpins;
        this.numberOfSongsListenedTo = numberOfSongsListenedTo;
        this.numberOfVideosPlayed = numberOfVideosPlayed;
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

    public int getNumberOfWikisChecked() {
        return numberOfWikisChecked;
    }

    public void setNumberOfWikisChecked(int numberOfWikisChecked) {
        this.numberOfWikisChecked = numberOfWikisChecked;
    }

    public int getNumberOfPolls() {
        return numberOfPolls;
    }

    public void setNumberOfPolls(int numberOfPolls) {
        this.numberOfPolls = numberOfPolls;
    }

    public int getNumberOfGifsSent() {
        return numberOfGifsSent;
    }

    public void setNumberOfGifsSent(int numberOfGifsSent) {
        this.numberOfGifsSent = numberOfGifsSent;
    }

    public int getNumberOfDiceRolls() {
        return numberOfDiceRolls;
    }

    public void setNumberOfDiceRolls(int numberOfDiceRolls) {
        this.numberOfDiceRolls = numberOfDiceRolls;
    }

    public int getNumberOfCoinFlips() {
        return numberOfCoinFlips;
    }

    public void setNumberOfCoinFlips(int numberOfCoinFlips) {
        this.numberOfCoinFlips = numberOfCoinFlips;
    }

    public int getNumberOfCatHoroscopes() {
        return numberOfCatHoroscopes;
    }

    public void setNumberOfCatHoroscopes(int numberOfCatHoroscopes) {
        this.numberOfCatHoroscopes = numberOfCatHoroscopes;
    }

    public int getNumberOfSteamRouletteSpins() {
        return numberOfSteamRouletteSpins;
    }

    public void setNumberOfSteamRouletteSpins(int numberOfSteamRouletteSpins) {
        this.numberOfSteamRouletteSpins = numberOfSteamRouletteSpins;
    }

    public int getNumberOfSongsListenedTo() {
        return numberOfSongsListenedTo;
    }

    public void setNumberOfSongsListenedTo(int numberOfSongsListenedTo) {
        this.numberOfSongsListenedTo = numberOfSongsListenedTo;
    }

    public int getNumberOfVideosPlayed() {
        return numberOfVideosPlayed;
    }

    public void setNumberOfVideosPlayed(int numberOfVideosPlayed) {
        this.numberOfVideosPlayed = numberOfVideosPlayed;
    }
}
