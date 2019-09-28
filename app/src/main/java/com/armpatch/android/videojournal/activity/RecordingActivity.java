package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
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
import com.armpatch.android.videojournal.model.ThumbnailFactory;

import java.util.UUID;

public class RecordingActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    public static final String TAG = "TAG_LOG";

    Recording recording;

    VideoView videoView;
    TextView dateText;
    EditText recordingTitleText, songTitleText, notesText;
    Button createButton;

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
        createButton = findViewById(R.id.create_button);
    }

    private void getRecording() {
        UUID uuid = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);

        if (uuid == null) {
            recording = new Recording();
        } else {
            recording = RecordingFactory.get(this).getRecording(uuid);
            videoView.setVideoPath(recording.getVideoPath());
            videoView.seekTo(1);
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
                if (recording.containsVideo()) {
                    videoView.start();
                } else {
                    takeVideo();
                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewRecording();
            }
        });
    }

    private void takeVideo() {
        VideoCamera.takeVideoAndSaveTo(recording, this);
    }

    private void saveNewRecording() {
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
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            recording.setThumbnailPath(ThumbnailFactory.createThumbnail(this, recording));

            videoView.setVideoPath(recording.getVideoPath());
            videoView.seekTo(1);

        }
    }
}
