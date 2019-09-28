package com.armpatch.android.videojournal.recyclerview;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.armpatch.android.videojournal.PictureUtils;
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
                400,
                400); // TODO dimensions shouldn't be hard coded
        // thumbnailView.setImageBitmap(thumbnail);
        // thumbnailView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(thumbnailView.getResources(), thumbnail);

        dr.setCornerRadius(4);
        thumbnailView.setImageDrawable(dr);
    }

    @Override
    public void onClick(View v) {
        callbacks.onRecordingSelected(recording);
    }
}
