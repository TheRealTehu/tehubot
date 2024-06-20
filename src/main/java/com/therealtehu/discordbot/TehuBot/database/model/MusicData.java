package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class MusicData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String author;
    private int numberOfPlay;

    public MusicData() {
    }

    public MusicData(long id, String title, String author, int numberOfPlay) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.numberOfPlay = numberOfPlay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumberOfPlay() {
        return numberOfPlay;
    }

    public void setNumberOfPlay(int numberOfPlay) {
        this.numberOfPlay = numberOfPlay;
    }
}
