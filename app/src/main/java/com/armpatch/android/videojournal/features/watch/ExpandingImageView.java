package com.armpatch.android.videojournal.features.watch;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;

import org.jetbrains.annotations.Nullable;

public class ExpandingImageView extends android.support.v7.widget.AppCompatImageView {

    ObjectAnimator animator = ExpandingAppearAnimator.get(this);

    public ExpandingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void showExpandingAnimation() {
        animator.start();
    }
}
