package com.armpatch.android.videojournal.model;

import java.util.Date;
import java.util.UUID;

public class Recording {

    public static final String EXTRA_KEY = "com.armpatch.android.videojournal.model.Recording";

    private UUID uuid;
    public String recordingTitle;
    public String songTitle;
    public Date date;
    public String notes;
    public String videoPath;

    public Recording() {
        this(UUID.randomUUID());
    }

    public Recording(UUID uuid) {
        this.uuid = uuid;
        date = new Date();
    }

    public UUID getId() {
        return uuid;
    }
}
