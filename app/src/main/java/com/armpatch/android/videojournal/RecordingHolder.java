package com.armpatch.android.videojournal;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class RecordingHolder extends RecyclerView.ViewHolder {

    private Recording recording;

    private TextView title;
    private TextView artist;
    private TextView date;

    public RecordingHolder() {

    }

    public void bind(Recording recording) {
        this.recording = recording;
        title.setText(recording.getTitle());
        artist.setText(recording.getArtist());

        date.setText("Created on: " + TextFormatter.getSimpleDate(recording.getDate()));
    }
}
