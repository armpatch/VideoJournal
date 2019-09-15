package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.armpatch.android.videojournal.model.Recording;

import java.util.UUID;

public class RecordingActivity extends AppCompatActivity {

    UUID recordingId;

    VideoView videoView;

    public static Intent newIntent(Context packageContext, UUID recordingId) {
        Intent intent = new Intent(packageContext, RecordingActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(Recording.EXTRA_KEY, recordingId);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        recordingId = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);
        if (recordingId == null) {








        }

        super.onCreate(savedInstanceState);
    }


}