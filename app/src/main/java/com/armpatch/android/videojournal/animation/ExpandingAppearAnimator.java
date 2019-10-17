package com.armpatch.android.videojournal.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class ExpandingAppearAnimator {

    public static ObjectAnimator get(final View view) {

        final float scaleStart = 0.5f;
        final float scaleEnd = 1.0f;
        int duration = 300;

        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, scaleStart, scaleEnd);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleStart, scaleEnd);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY);

        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(duration);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });

        return animator;
    }
}
