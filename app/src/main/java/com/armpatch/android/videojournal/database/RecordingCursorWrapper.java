package com.armpatch.android.videojournal.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.armpatch.android.videojournal.model.Recording;

import com.armpatch.android.videojournal.database.DatabaseSchema.RecordingTable;

import java.util.Date;
import java.util.UUID;

public class RecordingCursorWrapper extends CursorWrapper {
    public RecordingCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recording getRecording() {
        String uuidString = getString(getColumnIndex(RecordingTable.Cols.RECORDING_UUID));
        String recordingTitle = getString(getColumnIndex(RecordingTable.Cols.RECORDING_TITLE));
        String songTitle = getString(getColumnIndex(RecordingTable.Cols.SONG_TITLE));
        String notes = getString(getColumnIndex(RecordingTable.Cols.NOTES));
        long date = getLong(getColumnIndex(RecordingTable.Cols.DATE));
        String videoPath = getString(getColumnIndex(RecordingTable.Cols.VIDEO_PATH));

        Recording recording = new Recording(UUID.fromString(uuidString));
        recording.recordingTitle = recordingTitle;
        recording.songTitle = songTitle;
        recording.notes = notes;
        recording.date = new Date(date);
        recording.setVideoPath(videoPath);

        return recording;
    }
}