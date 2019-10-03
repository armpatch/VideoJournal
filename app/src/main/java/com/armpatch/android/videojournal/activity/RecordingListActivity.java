package com.armpatch.android.videojournal.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.RecordingFactory;
import com.armpatch.android.videojournal.recyclerview.RecordingAdapter;
import com.armpatch.android.videojournal.recyclerview.RecordingHolder;

import java.util.ArrayList;
import java.util.List;

public class RecordingListActivity extends AppCompatActivity implements RecordingHolder.Callbacks {

    private static final int REQUEST_WRITE_ACCESS_CODE = 1;

    RecyclerView recyclerView;
    private RecordingAdapter recordingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_list_activity);
        checkPermissions();

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recording_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_new_recording) {
            startActivity(RecordingActivity.newIntent(this, null));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecordingSelected(Recording recording) {
        //startActivity(RecordingActivity.newIntent(this, recording.getId()));
    }

    private boolean checkPermissions() {
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
            return false;
        }

        return true;
    }
}
