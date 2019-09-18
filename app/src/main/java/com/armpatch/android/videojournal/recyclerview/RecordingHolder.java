package com.armpatch.android.videojournal.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.TextFormatter;
import com.armpatch.android.videojournal.model.Recording;

public class RecordingHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public interface Callbacks {
        void onRecordingSelected(Recording recording);
    }

    private Callbacks callbacks;
    private Recording recording;

    private TextView recordingTitle;
    private TextView songTitle;
    private TextView date;

    public RecordingHolder(View view) {
        super(view);

        callbacks = (Callbacks) view.getContext();
        itemView.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        callbacks.onRecordingSelected(recording);
    }
}
