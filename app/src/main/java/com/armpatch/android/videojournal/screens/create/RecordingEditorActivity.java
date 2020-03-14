package com.armpatch.android.videojournal.screens.create;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.data.model.Recording;
import com.armpatch.android.videojournal.data.model.RecordingFactory;

import java.io.File;
import java.util.List;

public class RecordingEditorActivity extends AppCompatActivity{

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_CHOOSE_VIDEO = 2;
    public static final String ACTION_RECORD_VIDEO = "Record Video";
    public static final String ACTION_CHOOSE_VIDEO = "Choose Video";
    private static final String EXTRA_KEY = "RecordingEditorActivity.Action";

    private Recording recording;
    private EditText titleText;
    Button createButton;
    ImageView thumbnailView;

    public static Intent getIntent(Context activityContext, String action) {
        Intent intent = new Intent(activityContext, RecordingEditorActivity.class);

        Bundle extras = new Bundle();
        extras.putSerializable(EXTRA_KEY, action);
        return intent.putExtras(extras);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recording_editor);

        thumbnailView = findViewById(R.id.thumbnail);
        titleText = findViewById(R.id.recording_title);
        createButton = findViewById(R.id.create_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createButton.setClickable(false);
                saveRecording();
                finish();
            }
        });

        String action = (String) getIntent().getExtras().getSerializable(EXTRA_KEY);
        if (ACTION_RECORD_VIDEO.equals(action)) takeVideo();
        if (ACTION_CHOOSE_VIDEO.equals(action)) chooseVideo();
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

    private void chooseVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/mp4");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    REQUEST_CHOOSE_VIDEO);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRecording() {
        String titleUserEntered = titleText.getText().toString();

        if (titleUserEntered.isEmpty()) {
            recording.title = "New Recording";
        } else {
            recording.title = titleUserEntered;
        }

        RecordingFactory.get(this).addRecording(recording);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            String thumbnailPath = ThumbnailFactory.createThumbnail(this, recording);

            recording.setThumbnailPath(thumbnailPath);
            thumbnailView.setImageDrawable(new BitmapDrawable(getResources(), recording.getThumbnailPath()));

            titleText.requestFocus();

            // show the keyboard after a delay of .1 seconds after returning from recorder TODO find better solution
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(titleText, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);
            return;
        }

        if (requestCode == REQUEST_CHOOSE_VIDEO && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            File file = new File(uri.getPath()); //create path from uri
            final String[] split = file.getPath().split(":");//split the path.
            String sourcePath = split[1]; //assign it to a string(your choice).

            Toast.makeText(this, "path= " + sourcePath, Toast.LENGTH_SHORT).show();

            recording = new Recording();
            File outputFile = new File(getFilesDir(), recording.getVideoFilename());
            recording.setVideoPath(outputFile.getAbsolutePath());

            String outputPath = recording.getVideoPath();
            if (outputPath == null) {
                Log.d("TAG", "output path was null");
                return;
            }

            FileCopier.copy(sourcePath, outputPath);
        }

        onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
