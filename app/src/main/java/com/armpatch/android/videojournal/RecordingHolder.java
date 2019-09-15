package com.armpatch.android.videojournal;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.armpatch.android.videojournal.activities.Recording;

public class RecordingHolder extends RecyclerView.ViewHolder {


    private Recording recording;

    private TextView recordingTitle;
    private TextView songTitle;
    private TextView date;

    public RecordingHolder(Context context, LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.recording_list_item, parent, false));

        recordingTitle = itemView.findViewById(R.id.recording_title);
        songTitle = itemView.findViewById(R.id.song_title);
        date = itemView.findViewById(R.id.date);
    }

    public void bind(Recording recording) {
        this.recording = recording;
        recordingTitle.setText(recording.recordingTitle);
        songTitle.setText(recording.songTitle);

        String dateString = TextFormatter.getSimpleDateString(recording.date);
        date.setText(dateString);
    }
}
