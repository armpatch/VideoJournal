package com.armpatch.android.videojournal.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.model.ThumbnailFactory;
import com.armpatch.android.videojournal.util.PictureUtils;

public class RecordingDialog extends Dialog {

    private Context activityContext;
    private Recording recording;

    private EditText recordingTitleText, notesText;


    public RecordingDialog( Context activityContext, Recording recording) {
        super(activityContext);
        this.activityContext = activityContext;
        this.recording = recording;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recording_dialog);

        //create Thumbnail
        recording.setThumbnailPath(ThumbnailFactory.createThumbnail(activityContext, recording));

        //set Thumbnail
        ImageView thumbnailView = findViewById(R.id.thumbnail);
        Bitmap thumbnail = PictureUtils.getScaledBitmap(
                recording.getThumbnailPath(),
                400,
                400); // TODO: dimensions probably shouldn't be hard coded here

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(thumbnailView.getResources(), thumbnail);

        dr.setCornerRadius(4);
        thumbnailView.setImageDrawable(dr);

        // set text views and button
        recordingTitleText = findViewById(R.id.recording_title);
        notesText = findViewById(R.id.notes);

        Button createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecording();
                dismiss();
            }
        });

    }

    private void saveRecording() {
        recording.recordingTitle = recordingTitleText.getText().toString();
        recording.notes = notesText.getText().toString();

        RecordingFactory.get(activityContext).addRecording(recording);
    }

}
