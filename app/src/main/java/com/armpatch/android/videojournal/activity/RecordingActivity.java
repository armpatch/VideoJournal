package com.armpatch.android.videojournal.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;
import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.model.ThumbnailFactory;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class RecordingActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    public static final String TAG = "TAG_LOG";

    Recording recording;

    VideoView videoView;
    TextView dateText;
    EditText recordingTitleText, notesText;
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
        takeVideo();

        setContentView(R.layout.recording_activity);
        findViewsById();
        setListeners();
    }

    private void takeVideo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        recording = new Recording();

        File outputFile = new File(getFilesDir(), recording.getVideoFilename());
        recording.setVideoPath(outputFile.getAbsolutePath());

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

    private void findViewsById() {
        videoView = findViewById(R.id.videoView);
        recordingTitleText = findViewById(R.id.recording_title);
        dateText = findViewById(R.id.date);
        notesText = findViewById(R.id.notes);
        createButton = findViewById(R.id.create_button);
    }

    private void setListeners() {
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (recording.containsVideo()) {
                videoView.start();
            }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndFinishActivity();
            }
        });
    }

    private void saveAndFinishActivity() {
        updateRecordingFromFields();

        RecordingFactory.get(this).addRecording(recording);
        finish();
    }

    private void updateRecordingFromFields() {
        recording.recordingTitle = recordingTitleText.getText().toString();
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

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode != RESULT_OK) {
            finish();
        }
    }

}
