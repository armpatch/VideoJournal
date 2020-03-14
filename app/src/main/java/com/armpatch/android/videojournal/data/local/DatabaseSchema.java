package com.armpatch.android.videojournal.data.local;

public class DatabaseSchema {
    public static final class RecordingTable {
        public static final String NAME = "recordings";

        public static final class Cols {
            public static final String RECORDING_UUID = "recording_id";
            public static final String RECORDING_TITLE = "recording_title";
            public static final String DATE = "date";
            public static final String NOTES = "notes";
            public static final String VIDEO_PATH = "video_path";
            public static final String THUMBNAIL_PATH = "thumbnail_path";
        }
    }
}