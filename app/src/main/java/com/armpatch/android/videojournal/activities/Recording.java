package com.armpatch.android.videojournal.activities;

import java.util.Date;
import java.util.UUID;

public class Recording {

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
