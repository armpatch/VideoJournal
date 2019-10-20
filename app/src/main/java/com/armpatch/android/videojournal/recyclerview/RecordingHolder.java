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
        implements View.OnClickListener, View.OnLongClickListener{

    public interface Callbacks {
        void onRecordingClicked(Recording recording, RecordingHolder holder);
        void onRecordingLongClicked(Recording recording);
    }

    private Callbacks callbacks;
    private Recording recording;

    public TextView title, date;
    public ImageView thumbnail;
    public ImageView scrimTop, scrimBottom;

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
        scrimTop = itemView.findViewById(R.id.grid_scrim_top);
        scrimBottom = itemView.findViewById(R.id.grid_scrim_bottom);
    }

    private void setListeners() {
        itemView.setOnClickListener(this);
    }

    void bind(Recording recording, int position) {
        this.recording = recording;
        title.setText(recording.title);

        String dateString = TextFormatter.getSimpleDateString(recording.date);
        date.setText(dateString);

        setThumbnail(recording);
        setViewMargin(position);
    }

    private void setThumbnail(Recording recording) {
        Bitmap thumbnail = PictureUtils.getScaledBitmap(
                recording.getThumbnailPath(),
                800,
                800); // TODO: dimensions probably shouldn't be hard coded here

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(this.thumbnail.getResources(), thumbnail);

        dr.setCornerRadius(4);
        this.thumbnail.setImageDrawable(dr);
    }

    private void setViewMargin(int position) {
        // TODO **assumes this ViewHolder is in a grid layout with exactly two columns**
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemView.getLayoutParams());

        int margin = 8;

        if (position % 2 == 0) { // if position is odd, holder is in the first grid column, assuming two columns are used
            marginLayoutParams.setMargins(0,0,margin,margin * 2);
        } else {
            marginLayoutParams.setMargins(margin,0,0,margin * 2);
        }

        itemView.setLayoutParams(marginLayoutParams);
    }

    @Override
    public void onClick(View v) {
        callbacks.onRecordingClicked(recording, this);
    }

    @Override
    public boolean onLongClick(View v) {
        callbacks.onRecordingLongClicked(recording);
        return true;
    }
}
