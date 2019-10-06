package com.armpatch.android.videojournal.model;

import java.util.Date;
import java.util.UUID;

public class Recording {

    public static final String EXTRA_KEY = "com.armpatch.android.videojournal.model.Recording";

    private UUID uuid;

    public String title;
    public Date date;
    public String notes;

    private String videoPath;
    private String thumbnailPath;

    public Recording() {
        this(UUID.randomUUID());
    }

    public Recording(UUID uuid) {
        this.uuid = uuid;
        title = "";
        date = new Date();
        notes = "";
        videoPath = "";
    }

    public String getVideoFilename() {
        String id = getId().toString();
        id = id.substring(0,15);
        return "VIDEO_" + id + ".mp4";
    }

    String getImageFilename() {
        String id = getId().toString();
        id = id.substring(0,15);
        return "VIDEO_THUMB_" + id + ".png";
    }

    public boolean containsVideo() {
        return (videoPath.length() > 0);
    }

    public UUID getId() {
        return uuid;
    }

    public void setVideoPath(String path) {
        this.videoPath = path;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
