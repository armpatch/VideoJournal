package com.armpatch.android.videojournal.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.recyclerview.RecordingAdapter;
import com.armpatch.android.videojournal.recyclerview.RecordingHolder;

import java.util.ArrayList;
import java.util.List;

public class RecordingListActivity extends AppCompatActivity implements RecordingHolder.Callbacks {

    RecyclerView recyclerView;
    FloatingActionButton fab;

    private RecordingAdapter recordingAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_list_activity);

        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RecordingEditorActivity.getIntent(RecordingListActivity.this);
                startActivity(intent);
            }
        });

        askForPermissions();
    }

    private void askForPermissions() {
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
            requestPermissions(permissionsNeeded.toArray(new String[0]), 100);
        }
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
    public void onRecordingClicked(Recording recording, RecordingHolder holder) {
        Intent intent = RecordingViewerActivity.getIntent(this, recording.getId());

        // set shared element transition pairs
        Pair<View, String> imagePair = new Pair<View, String>(holder.thumbnail, holder.thumbnail.getTransitionName());
        Pair<View, String> titlePair = new Pair<View, String>(holder.title, holder.title.getTransitionName());
        Pair<View, String> datePair = new Pair<View, String>(holder.date, holder.date.getTransitionName());
        Pair<View, String> scrimTopPair = new Pair<View, String>(holder.scrimTop, holder.scrimTop.getTransitionName());
        Pair<View, String> scrimBottomPair = new Pair<View, String>(holder.scrimBottom, holder.scrimBottom.getTransitionName());

        Bundle bundle = ActivityOptions
                .makeSceneTransitionAnimation(
                        this,
                        imagePair,
                        titlePair,
                        datePair,
                        scrimTopPair,
                        scrimBottomPair
                        )
                .toBundle();

        startActivity(intent, bundle);
    }

    @Override
    public void onRecordingLongClicked(Recording recording) {
        //TODO add highlight feature
    }
}
