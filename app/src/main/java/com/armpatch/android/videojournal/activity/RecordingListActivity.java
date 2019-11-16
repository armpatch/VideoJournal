package com.armpatch.android.videojournal.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.recyclerview.RecordingAdapter;
import com.armpatch.android.videojournal.recyclerview.RecordingHolder;

import java.util.ArrayList;
import java.util.List;

public class RecordingListActivity extends AppCompatActivity implements RecordingHolder.Callbacks {

    RecyclerView recyclerView;
    View revealFrame;
    FloatingActionButton floatingButton;

    private RecordingAdapter recordingAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_list_activity);

        revealFrame = findViewById(R.id.circular_reveal);
        recyclerView = findViewById(R.id.recycler_view);

        floatingButton = findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewEntryDialog();
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
        revealFrame.setVisibility(View.INVISIBLE);
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
        Pair<View, String> cardPair = new Pair<View, String>(holder.cardView, holder.cardView.getTransitionName());

        Bundle bundle = ActivityOptions
                .makeSceneTransitionAnimation(
                        this,
                        imagePair,
                        titlePair,
                        datePair,
                        cardPair
                        )
                .toBundle();

        startActivity(intent, bundle);
    }

    private void showNewEntryDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_new_entry);
        View folderButton = dialog.findViewById(R.id.folder_button_container);
        final View cameraButton = dialog.findViewById(R.id.camera_button_container);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                startActivity(RecordingEditorActivity.getIntent(RecordingListActivity.this));
            }
        });

        folderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecordingListActivity.this,
                        "FEATURE NOT AVAILABLE" ,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        dialog.show();
    }
}
