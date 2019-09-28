package com.armpatch.android.videojournal.recyclerview;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.armpatch.android.videojournal.PictureUtils;
import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.TextFormatter;
import com.armpatch.android.videojournal.model.Recording;
import com.armpatch.android.videojournal.model.ThumbnailFactory;

public class RecordingHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public interface Callbacks {
        void onRecordingSelected(Recording recording);
    }

    private Callbacks callbacks;
    private Recording recording;

    private TextView recordingTitle, songTitle, date;
    private ImageView thumbnailView;

    public RecordingHolder(View view) {
        super(view);

        callbacks = (Callbacks) view.getContext();
        itemView.setOnClickListener(this);
        recordingTitle = itemView.findViewById(R.id.recording_title);
        songTitle = itemView.findViewById(R.id.song_title);
        date = itemView.findViewById(R.id.date);
        thumbnailView = itemView.findViewById(R.id.thumbnail);
    }

    public void bind(Recording recording) {
        this.recording = recording;
        recordingTitle.setText(recording.recordingTitle);
        songTitle.setText(recording.songTitle);

        String dateString = TextFormatter.getSimpleDateString(recording.date);
        date.setText(dateString);

        Bitmap thumbnail = PictureUtils.getScaledBitmap(
                recording.getThumbnailPath(),
                300,
                300); // TODO this needs not be hard coded
        thumbnailView.setImageBitmap(thumbnail);
    }

    @Override
    public void onClick(View v) {
        callbacks.onRecordingSelected(recording);
    }
}
