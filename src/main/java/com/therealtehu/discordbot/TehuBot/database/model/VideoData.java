package com.therealtehu.discordbot.TehuBot.database.model;

import jakarta.persistence.*;

@Entity
public class VideoData{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long videoId;

    private String title;

    private String uploader;

    private String videoUrl;

    @ManyToOne
    private Guild guild;

    public VideoData() {
    }

    public VideoData(long id, long videoId, String title, String uploader, String videoUrl, Guild guild) {
        this.id = id;
        this.videoId = videoId;
        this.title = title;
        this.uploader = uploader;
        this.videoUrl = videoUrl;
        this.guild = guild;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videId) {
        this.videoId = videId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }
}
