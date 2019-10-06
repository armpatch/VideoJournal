package com.armpatch.android.videojournal.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;

public class RecordingViewerDialog extends Dialog {

    private Context activityContext;
    private Recording recording;

    private TextView recordingTitleText, notesText;
    private VideoView videoView;

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

        recordingTitleText = findViewById(R.id.recording_title);
        recordingTitleText.setText(recording.title);

        notesText = findViewById(R.id.notes);
        notesText.setText(recording.notes);
    }


}
