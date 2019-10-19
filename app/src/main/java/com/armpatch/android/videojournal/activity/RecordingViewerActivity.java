package com.armpatch.android.videojournal.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.animation.FadeAnimator;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.util.TextFormatter;
import com.armpatch.android.videojournal.view.ExpandingImageView;
import com.armpatch.android.videojournal.view.RecordingVideoView;

import java.util.UUID;

public class RecordingViewerActivity extends AppCompatActivity {

    private static final String TAG = "RecordingViewerActivity";

    private TextView title, date;
    private RecordingVideoView videoView;
    private ImageView placeholder;
    private FrameLayout videoPane;
    private ExpandingImageView expandingPlayIcon;
    private ExpandingImageView expandingPauseIcon;

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
        setupViews();
        setupVideoPane();
        loadVideoFileInSeparateThread();

    }

    private void getRecordingFromIntent() {
        UUID recordingId = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);
        recording = RecordingFactory.get(this).getRecording(recordingId);

        if (recording == null) {
            finish();
        }
    }

    private void setupViews() {
        title = findViewById(R.id.recording_title);
        title.setText(recording.title);

        date = findViewById(R.id.date);
        date.setText(TextFormatter.getSimpleDateString(recording.date));

        expandingPlayIcon = findViewById(R.id.play_icon);
        expandingPauseIcon = findViewById(R.id.pause_icon);
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
                    // placeholderHideAnimator.start();
                    placeholder.setVisibility(View.INVISIBLE);
                }

                toggleVideoPlayback();
            }
        });
    }

    private void loadVideoFileInSeparateThread() {
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

        // prevents shared transitions when returning to previous activity
        placeholder.setTransitionName(null);
        title.setTransitionName(null);
        date.setTransitionName(null);

        super.onBackPressed();
    }

    private void toggleVideoPlayback() {
        if (videoView.isPlaying()) {
            videoView.pause();
            expandingPauseIcon.showExpandingAnimation();
        } else {
            videoView.start();
            expandingPlayIcon.showExpandingAnimation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recording_viewer, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_recording) {
            confirmDeleteRecording();
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmDeleteRecording() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        RecordingFactory.get(RecordingViewerActivity.this).deleteRecording(recording);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }
}
