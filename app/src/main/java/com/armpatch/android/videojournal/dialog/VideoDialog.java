package com.armpatch.android.videojournal.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.VideoView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;

public class VideoDialog {

    private VideoView videoView;
    private Dialog dialog;

    public VideoDialog(Context activityContext, Recording recording) {

        dialog = new Dialog(activityContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_video);

        videoView = dialog.findViewById(R.id.videoview);

        videoView.setVideoPath(recording.getVideoPath());
        videoView.seekTo(1);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });
    }

    public void show() {
        dialog.show();
    }
}
