package com.armpatch.android.videojournal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.armpatch.android.videojournal.activities.Recording;

import java.util.List;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingHolder> {

    private Context activityContext;
    private List<Recording> recordings;

    RecordingAdapter(Context activityContext, List<Recording> recordings) {
        this.activityContext = activityContext;
        this.recordings = recordings;
    }

    @NonNull
    @Override
    public RecordingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(activityContext);
        return new RecordingHolder(activityContext, inflater, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingHolder holder, int position) {
        holder.bind(recordings.get(position));
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    public void setSongs(List<Recording> recordings) {
        this.recordings = recordings;
    }
}
