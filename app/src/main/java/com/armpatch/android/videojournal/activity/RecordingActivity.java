package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.TextFormatter;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;

import java.util.UUID;

public class RecordingActivity extends AppCompatActivity {

    private static final String EXTRA_KEY_ID = "activity.RecordingActivity.id";

    Recording recording;

    VideoView videoView;
    TextView recordingTitleText, songTitleText, dateText, notesText;
    Button cancelButton, createButton;

    public static Intent newIntent(Context packageContext, UUID recordingId) {
        Intent intent = new Intent(packageContext, RecordingActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(Recording.EXTRA_KEY, recordingId);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.recording_activity);
        findViewsById();
        getRecording();
        updateViews();

        super.onCreate(savedInstanceState);
    }

    private void findViewsById() {
        videoView = findViewById(R.id.videoView);
        recordingTitleText = findViewById(R.id.recording_title);
        songTitleText = findViewById(R.id.song_title);
        dateText = findViewById(R.id.date);
        notesText = findViewById(R.id.notes);
        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToSaveRecording();
            }
        });
    }

    private void attemptToSaveRecording() {
        if (recording.videoPath == null) {
            finish();
        }
        RecordingFactory.get(this).addRecording(recording);
        finish();
    }

    private void getRecording() {
        UUID recordingID = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);

        if (recordingID == null) {
            createNewRecording();
        } else {
            recording = RecordingFactory.get(this).getRecording(recordingID);
        }
    }

    private void createNewRecording() {
        recording = new Recording();
        recording.recordingTitle = "Recording Title";
        recording.songTitle = "Song Title";
        recording.notes = "recording notes...";
    }

    private void updateViews() {
        recordingTitleText.setText(recording.recordingTitle);
        songTitleText.setText(recording.songTitle);
        dateText.setText(TextFormatter.getSimpleDateString(recording.date));
        notesText.setText(recording.notes);
    }
}