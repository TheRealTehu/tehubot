package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "GUILD_STATISTICS_DATA")
public class GuildStatisticsData {
    @Id
    private long guildId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private GuildData guild;
    private int numberOfDiceRolls;
    private int numberOfCoinFlips;
    private int numberOfWikisChecked;
    private int numberOfGifsSent;
    private int numberOfPolls;

    //    private int numberOfCatHoroscopes;
//    private int numberOfSteamRouletteSpins;
//    private int numberOfSongsListenedTo;
//    private int numberOfVideosPlayed;
    public GuildStatisticsData() {
    }

    public GuildStatisticsData(long guildId, GuildData guild, int numberOfWikisChecked, int numberOfGifsSent,
                               int numberOfDiceRolls, int numberOfCoinFlips, int numberOfPolls /*,
                               int numberOfCatHoroscopes, int numberOfSteamRouletteSpins,
                               int numberOfSongsListenedTo, int numberOfVideosPlayed*/) {
        this.guildId = guildId;
        this.guild = guild;
        this.numberOfDiceRolls = numberOfDiceRolls;
        this.numberOfCoinFlips = numberOfCoinFlips;
        this.numberOfWikisChecked = numberOfWikisChecked;
        this.numberOfGifsSent = numberOfGifsSent;
        this.numberOfPolls = numberOfPolls;
        /*this.numberOfCatHoroscopes = numberOfCatHoroscopes;
        this.numberOfSteamRouletteSpins = numberOfSteamRouletteSpins;
        this.numberOfSongsListenedTo = numberOfSongsListenedTo;
        this.numberOfVideosPlayed = numberOfVideosPlayed;*/
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long id) {
        this.guildId = id;
    }

    public GuildData getGuild() {
        return guild;
    }

    public void setGuild(GuildData guild) {
        this.guild = guild;
    }


    public int getNumberOfWikisChecked() {
        return numberOfWikisChecked;
    }

    public void setNumberOfWikisChecked(int numberOfWikisChecked) {
        this.numberOfWikisChecked = numberOfWikisChecked;
    }

    public int getNumberOfGifsSent() {
        return numberOfGifsSent;
    }

    public void setNumberOfGifsSent(int numberOfGifsSent) {
        this.numberOfGifsSent = numberOfGifsSent;
    }

    public int getNumberOfPolls() {
        return numberOfPolls;
    }

    public void setNumberOfPolls(int numberOfPolls) {
        this.numberOfPolls = numberOfPolls;
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

    /*
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
    */
    @Override
    public String toString() {
        return "GuildStatisticsData{" +
                "id=" + guildId +
                ", guild=" + guild +
                ", numberOfDiceRolls=" + numberOfDiceRolls +
                ", numberOfCoinFlips=" + numberOfCoinFlips +
                ", numberOfWikisChecked=" + numberOfWikisChecked +
                ", numberOfGifsSent=" + numberOfGifsSent +
                ", numberOfPolls=" + numberOfPolls /* +
                ", numberOfCatHoroscopes=" + numberOfCatHoroscopes +
                ", numberOfSteamRouletteSpins=" + numberOfSteamRouletteSpins +
                ", numberOfSongsListenedTo=" + numberOfSongsListenedTo +
                ", numberOfVideosPlayed=" + numberOfVideosPlayed */ +
                '}';
    }
}
