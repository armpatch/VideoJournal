package com.armpatch.android.videojournal.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.RecordingAdapter;
import com.armpatch.android.videojournal.RecordingFactory;

import java.util.List;

public class RecordingListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecordingAdapter recordingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_list_activity);

        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recording_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_recording:
                // TODO show new songs page
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}
