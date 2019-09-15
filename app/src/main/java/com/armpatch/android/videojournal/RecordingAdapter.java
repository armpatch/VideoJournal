package com.armpatch.android.videojournal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class RecordingAdapter extends RecyclerView.Adapter {

    private Context activityContext;
    private List<Recording> recordingList;

    RecordingAdapter(Context activityContext) {
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(activityContext);
        return new RecordingHolder();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        // do nothing
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
