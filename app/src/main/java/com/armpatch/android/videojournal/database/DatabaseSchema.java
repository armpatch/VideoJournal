package com.armpatch.android.videojournal.database;

public class DatabaseSchema {
    public static final class RecordingTable {
        public static final String NAME = "recordings";

        public static final class Cols {
            public static final String RECORDING_UUID = "recording_id";
            public static final String RECORDING_TITLE = "recording_title";
            public static final String SONG_TITLE = "song_title";
            public static final String DATE = "date";
            public static final String NOTES = "notes";
            public static final String VIDEO_PATH = "path";
        }
    }
}