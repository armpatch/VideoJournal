package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.util.TextFormatter;

import java.util.UUID;

public class RecordingViewerActivity extends AppCompatActivity {

    private TextView title, date;
    private VideoView videoView;
    private ImageView scrimTop, scrimBottom;

    private Recording recording;

    public static Intent getIntent(Context activityContext, UUID recordingId) {
        Intent intent = new Intent(activityContext, RecordingViewerActivity.class);

        Bundle extras = new Bundle();
        extras.putSerializable(Recording.EXTRA_KEY, recordingId);

        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRecording();

        setContentView(R.layout.recording_viewer_activity);

        videoView = findViewById(R.id.videoview);
        title = findViewById(R.id.recording_title);
        title.setText(recording.title);

        date = findViewById(R.id.date);
        date.setText(TextFormatter.getSimpleDateString(recording.date));

        videoView.setVideoPath(recording.getVideoPath());
        videoView.seekTo(1);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
            }
        });

        scrimTop = findViewById(R.id.scrim_top);
        scrimBottom = findViewById(R.id.scrim_bottom);
    }

    private void getRecording() {
        UUID recordingId = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);
        recording = RecordingFactory.get(this).getRecording(recordingId);

        if (recording == null) {
            finish();
        }
    }
}
