package com.armpatch.android.videojournal.features.home;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.armpatch.android.videojournal.R;
import com.armpatch.android.videojournal.domain.Recording;
import com.armpatch.android.videojournal.tools.TextFormatter;

public class RecordingHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    public interface Callbacks {
        void onRecordingClicked(Recording recording, RecordingHolder holder);
    }

    private Callbacks callbacks;
    private Recording recording;

    public TextView title, date;
    public ImageView thumbnail;
    public CardView cardView;

    RecordingHolder(View view) {
        super(view);

        callbacks = (Callbacks) view.getContext();

        findViewsById();
        setListeners();
    }

    private void findViewsById() {
        title = itemView.findViewById(R.id.recording_title);
        date = itemView.findViewById(R.id.date);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        cardView = itemView.findViewById(R.id.list_item_card);
    }

    private void setListeners() {
        itemView.setOnClickListener(this);
    }

    public void bind(Recording recording) {
        this.recording = recording;
        title.setText(recording.title);

        String dateString = TextFormatter.getSimpleDateString(recording.date);
        date.setText(dateString);

        setThumbnail(recording);
    }

    private void setThumbnail(Recording recording) {
        Bitmap thumbnail = PictureUtils.getScaledBitmap(
                recording.getThumbnailPath(),
                600,
                600); // TODO: dimensions probably shouldn't be hard coded here

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(this.thumbnail.getResources(), thumbnail);

        this.thumbnail.setImageDrawable(dr);
    }

    @Override
    public void onClick(View v) {
        callbacks.onRecordingClicked(recording, this);
    }
}
