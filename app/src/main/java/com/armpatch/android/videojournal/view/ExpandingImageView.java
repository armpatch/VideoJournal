package com.armpatch.android.videojournal.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;

import com.armpatch.android.videojournal.animation.ExpandingAppearAnimator;

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
