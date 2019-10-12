package com.armpatch.android.videojournal.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.dialog.RecordingEditorDialog;
import com.armpatch.android.videojournal.dialog.RecordingViewerDialog;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.recyclerview.RecordingAdapter;
import com.armpatch.android.videojournal.recyclerview.RecordingHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordingListActivity extends AppCompatActivity implements RecordingHolder.Callbacks {

    private static final int REQUEST_VIDEO_CAPTURE = 1;

    RecyclerView recyclerView;
    FloatingActionButton fab;

    private RecordingAdapter recordingAdapter;

    Recording recordingSlate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_list_activity);

        checkPermissions();

        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVideo();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        RecordingFactory recordingFactory = RecordingFactory.get(this);
        List<Recording> recordings = recordingFactory.getRecordings();

        if (recordingAdapter == null) {
            recordingAdapter = new RecordingAdapter(this, recordings);
            recyclerView.setAdapter(recordingAdapter);
        } else {
            recordingAdapter.setRecordings(recordings);
            recordingAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRecordingSelected(Recording recording) {
        RecordingViewerDialog dialog = new RecordingViewerDialog(this, recording);
        dialog.show();
    }

    private void checkPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.VIBRATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAPTURE_VIDEO_OUTPUT,
                Manifest.permission.CAMERA

        };

        int result;
        List<String> permissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = checkSelfPermission(p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(p);
            }
        }

        if (!permissionsNeeded.isEmpty()) {
            requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), 100);
        }
    }

    private void takeVideo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        recordingSlate = new Recording();

        File outputFile = new File(getFilesDir(), recordingSlate.getVideoFilename());
        recordingSlate.setVideoPath(outputFile.getAbsolutePath());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

            RecordingEditorDialog dialog = new RecordingEditorDialog(this, recordingSlate);
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    updateRecyclerView();
                }
            });
        }
    }
}
