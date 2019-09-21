package com.armpatch.android.videojournal.model;

import android.net.Uri;

import java.util.Date;
import java.util.UUID;

public class Recording {

    public static final String EXTRA_KEY = "com.armpatch.android.videojournal.model.Recording";

    private UUID uuid;

    public String recordingTitle;
    public String songTitle;
    public Date date;
    public String notes;
    public String path;

    public Recording() {
        this(UUID.randomUUID());
    }

    public Recording(UUID uuid) {
        this.uuid = uuid;
        recordingTitle = "";
        songTitle = "";
        date = new Date();
        notes = "";
        path = "";
    }

    public UUID getId() {
        return uuid;
    }

    public boolean containsVideo() {
        return (path.length() > 0);
    }

    public void setPath(Uri uri) {
        this.path = uri.toString();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
