package com.armpatch.android.videojournal.storage;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.armpatch.android.videojournal.domain.Recording;

import com.armpatch.android.videojournal.storage.DatabaseSchema.RecordingTable;

import java.util.Date;
import java.util.UUID;

public class RecordingCursorWrapper extends CursorWrapper {
    public RecordingCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recording getRecording() {
        String uuidString = getString(getColumnIndex(RecordingTable.Cols.RECORDING_UUID));
        String recordingTitle = getString(getColumnIndex(RecordingTable.Cols.RECORDING_TITLE));
        String notes = getString(getColumnIndex(RecordingTable.Cols.NOTES));
        long date = getLong(getColumnIndex(RecordingTable.Cols.DATE));
        String videoPath = getString(getColumnIndex(RecordingTable.Cols.VIDEO_PATH));
        String thumbnailPath = getString(getColumnIndex(RecordingTable.Cols.THUMBNAIL_PATH));

        Recording recording = new Recording(UUID.fromString(uuidString));
        recording.title = recordingTitle;
        recording.notes = notes;
        recording.date = new Date(date);
        recording.setVideoPath(videoPath);
        recording.setThumbnailPath(thumbnailPath);

        return recording;
    }
}