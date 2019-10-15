package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.model.ThumbnailFactory;
import com.armpatch.android.videojournal.util.PictureUtils;

import java.io.File;
import java.util.List;

public class RecordingEditorActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;

    private Recording recording;
    private EditText recordingTitleText, notesText;
    Button createButton;
    ImageView thumbnailView;

    public static Intent getIntent(Context activityContext) {
        return new Intent(activityContext, RecordingEditorActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recording_editor_activity);

        thumbnailView = findViewById(R.id.thumbnail);
        recordingTitleText = findViewById(R.id.recording_title);
        notesText = findViewById(R.id.notes);
        createButton = findViewById(R.id.create_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecording();
                finish();
            }
        });

        takeVideo();
    }

    private void takeVideo() {
        recording = new Recording();

        File outputFile = new File(getFilesDir(), recording.getVideoFilename());
        recording.setVideoPath(outputFile.getAbsolutePath());

        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri outputUri = FileProvider.getUriForFile(this, "com.armpatch.android.videojournal.fileprovider", outputFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        // grant uri permissions
        List<ResolveInfo> cameraActivities = getPackageManager().
                queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {
            grantUriPermission(activity.activityInfo.packageName, outputUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        // if a camera is available, start it
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    private void saveRecording() {
        String titleUserEntered = recordingTitleText.getText().toString();

        if (titleUserEntered.isEmpty()) {
            recording.title = "New Recording";
        } else {
            recording.title = titleUserEntered;
        }

        recording.notes = notesText.getText().toString();

        RecordingFactory.get(this).addRecording(recording);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            recording.setThumbnailPath(ThumbnailFactory.createThumbnail(this, recording));

            createThumbnail();
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode != RESULT_OK) {
            finish();
        }
    }

    private void createThumbnail() {
        Bitmap thumbnail = PictureUtils.getScaledBitmap(
                recording.getThumbnailPath(),
                800,
                800); // TODO: dimensions should not be hard coded here

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(thumbnailView.getResources(), thumbnail);

        dr.setCornerRadius(4);
        thumbnailView.setImageDrawable(dr);
    }

}
