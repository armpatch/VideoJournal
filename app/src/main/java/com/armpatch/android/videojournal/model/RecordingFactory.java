package com.armpatch.android.videojournal.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.armpatch.android.videojournal.database.RecordingCursorWrapper;
import com.armpatch.android.videojournal.database.RecordingDbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.armpatch.android.videojournal.database.DatabaseSchema.RecordingTable.Cols.*;
import static com.armpatch.android.videojournal.database.DatabaseSchema.RecordingTable.NAME;

public class RecordingFactory {

    private static RecordingFactory recordingFactory;
    private SQLiteDatabase database;
    private Context appContext;

    public static RecordingFactory get(Context context) {
        if (recordingFactory == null) {
            recordingFactory = new RecordingFactory(context);
        }

        return recordingFactory;
    }

    private RecordingFactory(Context context) {
        database = new RecordingDbHelper(context).getWritableDatabase();
        appContext = context;
    }

    public List<Recording> getRecordings() {
        List<Recording> recordings = new ArrayList<>();
        RecordingCursorWrapper cursor = querySongs( null,null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recordings.add(cursor.getRecording());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return recordings;
    }

    public Recording getRecording(UUID id) {
        String[] recordingId = new String[]{id.toString()};
        String whereClause = RECORDING_UUID + " = ?";

        RecordingCursorWrapper cursor = querySongs(whereClause, recordingId);
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getRecording();
        } finally {
            cursor.close();
        }
    }

    public void updateRecording(Recording recording) {
        String uuidString = recording.getId().toString();
        ContentValues values = getContentValues(recording);

        database.update(NAME, values,
                RECORDING_UUID + " = ?",
                new String[]{uuidString});
    }

    private RecordingCursorWrapper querySongs(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                NAME,
                null,
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new RecordingCursorWrapper(cursor);
    }

    public void addRecording(Recording recording) {
        ContentValues values = getContentValues(recording);
        database.insert(NAME, null, values);
    }

    private static ContentValues getContentValues(Recording recording) {
        ContentValues values = new ContentValues();
        values.put(RECORDING_UUID, recording.getId().toString());
        values.put(RECORDING_TITLE, recording.title);
        values.put(DATE, recording.date.getTime());
        values.put(NOTES, recording.notes);
        values.put(VIDEO_PATH, recording.getVideoPath());
        values.put(THUMBNAIL_PATH, recording.getThumbnailPath());

        return values;
    }

    public void deleteRecording(Recording recording) {
        String uuidString = recording.getId().toString();

        database.delete(NAME, RECORDING_UUID + " = ?",
                new String[]{uuidString});
    }
}
