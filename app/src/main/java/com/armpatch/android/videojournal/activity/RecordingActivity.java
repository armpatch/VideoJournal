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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;
import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.TextFormatter;
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
    EditText recordingTitleText, songTitleText, notesText;
    Button createButton, createBitmapButton;

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

        setContentView(R.layout.recording_activity);
        findViewsById();
        getRecording();
        setTextFieldsFromRecording();
        setListeners();
    }

    private void findViewsById() {
        videoView = findViewById(R.id.videoView);
        recordingTitleText = findViewById(R.id.recording_title);
        songTitleText = findViewById(R.id.song_title);
        dateText = findViewById(R.id.date);
        notesText = findViewById(R.id.notes);
        createButton = findViewById(R.id.create_button);
        createBitmapButton = findViewById(R.id.create_bitmap_button);
    }

    private void getRecording() {
        UUID uuid = (UUID) getIntent().getExtras().getSerializable(Recording.EXTRA_KEY);

        if (uuid == null) {
            recording = new Recording();
        } else {
            recording = RecordingFactory.get(this).getRecording(uuid);
            videoView.setVideoURI(Uri.parse(recording.getVideoPath()));
        }
    }

    private void setTextFieldsFromRecording() {
        recordingTitleText.setText(recording.recordingTitle);
        songTitleText.setText(recording.songTitle);
        dateText.setText(TextFormatter.getSimpleDateString(recording.date));
        notesText.setText(recording.notes);
    }

    private void setListeners() {
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording.getVideoPath().length() > 1) {
                    videoView.start();
                } else {
                    dispatchRecordIntent();
                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewRecordingAndFinish();
            }
        });

        createBitmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBitmap();
            }
        });
    }

    private void dispatchRecordIntent() {
        // create an intent with the output path as an extra
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File outputFile = new File(getFilesDir(), recording.getVideoFilename());
        Uri outputUri = FileProvider.getUriForFile(this, "com.armpatch.android.videojournal.fileprovider", outputFile);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        // grant uri permissions
        List<ResolveInfo> cameraActivities = getPackageManager().
                queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {
            grantUriPermission(activity.activityInfo.packageName, outputUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        // if a camera is available, start it
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void saveNewRecordingAndFinish() {
        if (recording.containsVideo()) {
            updateRecordingFromFields();

            RecordingFactory.get(this).addRecording(recording);
            finish();
        } else {
            // TODO alert the user
        }
    }

    private void updateRecordingFromFields() {
        recording.recordingTitle = recordingTitleText.getText().toString();
        recording.songTitle = songTitleText.getText().toString();
        recording.notes = notesText.getText().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoView.setVideoPath(recording.getVideoPath());

            Uri resultIntentUri = intent.getData();
            Log.v(TAG, "onActivityResult: uri = " + resultIntentUri.toString());
            recording.setVideoPath(resultIntentUri.toString());
            videoView.setVideoURI(resultIntentUri);
        }
    }

    private void createBitmap() {
        recording.setThumbnailPath(ThumbnailFactory.createThumbnail(this, recording));
    }
}
