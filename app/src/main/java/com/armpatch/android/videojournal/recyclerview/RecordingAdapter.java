package com.armpatch.android.videojournal.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.model.Recording;

import java.util.List;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingHolder> {

    private Context activityContext;
    private List<Recording> recordings;

public RecordingAdapter(Context activityContext, List<Recording> recordings) {
        this.activityContext = activityContext;
        this.recordings = recordings;
    }

    @NonNull
    @Override
    public RecordingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activityContext)
                .inflate(R.layout.list_item_recording, viewGroup, false);

        return new RecordingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingHolder holder, int position) {
        holder.bind(recordings.get(position));
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
    }
}
