package com.armpatch.android.videojournal.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Placeholder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.animation.FadeAnimator;
import com.armpatch.android.videojournal.view.RecordingVideoView;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.util.TextFormatter;

import java.util.UUID;

public class RecordingViewerActivity extends AppCompatActivity {

    private static final String TAG = "RecordingViewerActivity";

    private TextView title, date;
    private RecordingVideoView videoView;
    private ImageView scrimTop, scrimBottom, placeholder;
    private RelativeLayout videoPane;

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
        setContentView(R.layout.recording_viewer_activity);

        getRecordingFromIntent();
        setupDetails();

        setupVideoPane();

        loadVideoFile();
    }

    private void getRecordingFromIntent() {
        UUID recordingId = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);
        recording = RecordingFactory.get(this).getRecording(recordingId);

        if (recording == null) {
            finish();
        }
    }

    private void setupDetails() {
        title = findViewById(R.id.recording_title);
        title.setText(recording.title);

        date = findViewById(R.id.date);
        date.setText(TextFormatter.getSimpleDateString(recording.date));

        scrimTop = findViewById(R.id.scrim_top);
        scrimBottom = findViewById(R.id.scrim_bottom);
    }

    private void setupVideoPane() {
        /// PLACEHOLDER ///

        placeholder = findViewById(R.id.placeholder);
        placeholder.setImageDrawable(new BitmapDrawable(getResources(), recording.getThumbnailPath()));

        final ObjectAnimator placeholderHideAnimator = FadeAnimator.get(placeholder, 100);
        placeholderHideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                placeholder.setVisibility(View.GONE);
            }
        });

        /// VIDEO VIEW ///
        videoView = findViewById(R.id.videoview);
        videoView.setVisibility(View.GONE);

        videoPane = findViewById(R.id.video_pane);
        videoPane.setClickable(false);
        videoPane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeholder.getVisibility() == View.VISIBLE) {
                    videoView.setVisibility(View.VISIBLE);
                    placeholderHideAnimator.start();
                }

                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
            }
        });
    }

    private void loadVideoFile() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                videoView.setVideoPath(recording.getVideoPath());
                videoView.seekTo(1);

                videoPane.setClickable(true);
            }
        };
        runnable.run();
    }

    @Override
    public void onBackPressed() {
        videoView.pause();
        videoView.setVisibility(View.GONE);
        placeholder.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }

}
