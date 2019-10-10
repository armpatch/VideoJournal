package com.armpatch.android.videojournal.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;

public class RecordingViewerDialog extends Dialog {

    private Context activityContext;
    private Recording recording;

    private TextView recordingTitleText, notesText;
    private VideoView videoView;
    private ImageButton replayButton, playPauseButton;

    public RecordingViewerDialog(@NonNull Context activityContext, Recording recording) {
        super(activityContext);
        this.activityContext = activityContext;
        this.recording = recording;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recording_viewer_dialog);

        videoView = findViewById(R.id.videoview);
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
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                showPlayButton();
            }
        });


        replayButton = findViewById(R.id.replay_button);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
                videoView.seekTo(1);
                showPlayButton();
            }
        });

        playPauseButton = findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });

        recordingTitleText = findViewById(R.id.recording_title);
        recordingTitleText.setText(recording.title);

        notesText = findViewById(R.id.notes);
        notesText.setText(recording.notes);
    }

    private void togglePlayPause() {
        if (videoView.isPlaying()) {
            videoView.pause();
            showPlayButton();
        } else {
            videoView.start();
            showPauseButton();
        }
    }

    private void showPlayButton() {
        playPauseButton.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_play_icon_circle));

    }

    private void showPauseButton() {
        playPauseButton.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_pause_icon_circle));

    }

}
