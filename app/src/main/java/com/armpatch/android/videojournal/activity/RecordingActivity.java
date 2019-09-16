package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    TextView dateText;
    EditText recordingTitleText, songTitleText, notesText;
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
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recording_activity);
        findViewsById();
        getRecording();
        updateTextFieldsFromRecording();
        setListeners();
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
    }

    private void setListeners() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToSaveRecording();
            }
        });
    }

    private void attemptToSaveRecording() {
        if (recordingIsSavable()) {
            recording.recordingTitle = recordingTitleText.getText().toString();
            recording.songTitle = songTitleText.getText().toString();
            recording.notes = notesText.getText().toString();

            RecordingFactory.get(this).addRecording(recording);
        } else {
            // TODO alert the user
        }

        finish();
    }

    private boolean recordingIsSavable() {
        if (recording.videoPath == null)
            return false;
        if (recordingTitleText.getText().toString().length() < 1)
            return false;
        if (songTitleText.getText().toString().length() < 1)
            return false;


        return true;
    }

    private void getRecording() {
        UUID recordingID = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);

        if (recordingID == null) {
            recording = new Recording();
        } else {
            recording = RecordingFactory.get(this).getRecording(recordingID);
            videoView.setVideoPath(recording.videoPath);
        }
    }

    private void updateTextFieldsFromRecording() {
        recordingTitleText.setText(recording.recordingTitle);
        songTitleText.setText(recording.songTitle);
        dateText.setText(TextFormatter.getSimpleDateString(recording.date));
        notesText.setText(recording.notes);
    }
}