package com.armpatch.android.videojournal.recyclerview;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.armpatch.android.videojournal.util.PictureUtils;
import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.util.TextFormatter;
import com.armpatch.android.videojournal.model.Recording;

public class RecordingHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public interface Callbacks {
        void onRecordingSelected(Recording recording);
    }

    private Callbacks callbacks;
    private Recording recording;

    private TextView recordingTitle, date;
    private ImageView thumbnailView;

    RecordingHolder(View view) {
        super(view);

        callbacks = (Callbacks) view.getContext();

        findViewsById();
        setListeners();
    }

    private void findViewsById() {
        recordingTitle = itemView.findViewById(R.id.recording_title);
        date = itemView.findViewById(R.id.date);
        thumbnailView = itemView.findViewById(R.id.thumbnail);
    }

    private void setListeners() {
        itemView.setOnClickListener(this);
    }

    void bind(Recording recording, int position) {
        this.recording = recording;
        recordingTitle.setText(recording.title);

        String dateString = TextFormatter.getSimpleDateString(recording.date);
        date.setText(dateString);

        setThumbnail(recording);
        setViewMargin(position);
    }

    private void setThumbnail(Recording recording) {
        Bitmap thumbnail = PictureUtils.getScaledBitmap(
                recording.getThumbnailPath(),
                400,
                400); // TODO: dimensions probably shouldn't be hard coded here

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(thumbnailView.getResources(), thumbnail);

        dr.setCornerRadius(4);
        thumbnailView.setImageDrawable(dr);
    }

    private void setViewMargin(int position) {
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemView.getLayoutParams());

        int margin = 6;

        if (position % 2 == 0) {
            marginLayoutParams.setMargins(0,0,margin,margin * 2);
        } else {
            marginLayoutParams.setMargins(margin,0,0,margin * 2);
        }

        itemView.setLayoutParams(marginLayoutParams);
    }

    @Override
    public void onClick(View v) {
        callbacks.onRecordingSelected(recording);
    }
}
