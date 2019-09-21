package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    static final int REQUEST_VIDEO_CAPTURE = 1;

    public static final String TAG = "RecordingActivityTag";

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
        setTextFieldsFromRecording();
        setListeners();
    }

    private void findViewsById() {
        videoView = findViewById(R.id.videoView);
        recordingTitleText = findViewById(R.id.recording_title);
        songTitleText = findViewById(R.id.song_title);
        dateText = findViewById(R.id.date);
        notesText = findViewById(R.id.notes);
        cancelButton = findViewById(R.id.cancel_button);
        createButton = findViewById(R.id.create_button);
    }

    private void getRecording() {
        UUID uuid = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);

        if (uuid == null) {
            recording = new Recording();
        } else {
            recording = RecordingFactory.get(this).getRecording(uuid);
            videoView.setVideoURI(Uri.parse(recording.getPath()));
        }
    }

    private void setTextFieldsFromRecording() {
        recordingTitleText.setText(recording.recordingTitle);
        songTitleText.setText(recording.songTitle);
        dateText.setText(TextFormatter.getSimpleDateString(recording.date));
        notesText.setText(recording.notes);
    }

    private void setListeners() {
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording.getPath().length() > 1) {
                    videoView.start();
                } else {
                    dispatchRecordIntent();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSaveAndClose();
            }
        });
    }

    private void dispatchRecordIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void attemptSaveAndClose() {
        if (recording.containsVideo()) {
            updateRecordingFromFields();

            RecordingFactory.get(this).addRecording(recording);
            finish();
        } else {
            // TODO alert the user
        }
    }

    private void updateRecordingFromFields() {
        recording.recordingTitle = recordingTitleText.getText().toString();
        recording.songTitle = songTitleText.getText().toString();
        recording.notes = notesText.getText().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri uri = intent.getData();
            Log.v(TAG, "onActivityResult(): uri.toString = " + uri.toString());
            videoView.setVideoURI(uri);
            recording.setPath(uri);
        }
    }
}
