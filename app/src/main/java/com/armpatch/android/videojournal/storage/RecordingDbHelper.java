package com.armpatch.android.videojournal.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.armpatch.android.videojournal.storage.DatabaseSchema.RecordingTable;

public class RecordingDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "recordingBase.db";

    public RecordingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RecordingTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                RecordingTable.Cols.RECORDING_UUID + ", " +
                RecordingTable.Cols.RECORDING_TITLE + ", " +
                RecordingTable.Cols.DATE + "," +
                RecordingTable.Cols.NOTES + ", " +
                RecordingTable.Cols.VIDEO_PATH + "," +
                RecordingTable.Cols.THUMBNAIL_PATH +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
