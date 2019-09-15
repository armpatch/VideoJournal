package com.armpatch.android.videojournal.activities;

import java.util.Date;
import java.util.UUID;

public class Recording {

    private UUID uuid;
    private Date date;
    private String notes;
    private String videoPath;

    public Recording() {
        this(UUID.randomUUID());
    }

    public Recording(UUID id) {
        uuid = id;
        date = new Date();
    }
}
