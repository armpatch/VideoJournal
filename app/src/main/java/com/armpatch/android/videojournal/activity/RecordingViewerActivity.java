package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;

import java.util.UUID;

public class RecordingViewerActivity extends AppCompatActivity {

    private TextView title, notes;
    private VideoView videoView;

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


    }

    private void getRecording() {
        UUID recordingId = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);
        recording = RecordingFactory.get(this).getRecording(recordingId);

        if (recording == null) {
            finish();
        }
    }
}
