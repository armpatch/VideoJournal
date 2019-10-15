package com.armpatch.android.videojournal.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class FadeAnimator{
    public static ObjectAnimator get(final View view, int duration) {

        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, view.getAlpha(), 0.0f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, alpha);

        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);

        return animator;
    }
}
