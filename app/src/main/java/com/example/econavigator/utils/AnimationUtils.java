package com.example.econavigator.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationUtils {

    /**
     * Fade in animation
     */
    public static void fadeIn(View view) {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(Constants.ANIMATION_DURATION_MEDIUM);
        view.startAnimation(fadeIn);
        view.setVisibility(View.VISIBLE);
    }

    /**
     * Fade out animation
     */
    public static void fadeOut(View view) {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(Constants.ANIMATION_DURATION_MEDIUM);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(fadeOut);
    }

    /**
     * Scale animation (bounce effect)
     */
    public static void bounce(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(Constants.ANIMATION_DURATION_SHORT);
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(scaleAnimation);
    }

    /**
     * Slide up animation
     */
    public static void slideUp(View view) {
        TranslateAnimation slideUp = new TranslateAnimation(
                0, 0, view.getHeight(), 0
        );
        slideUp.setDuration(Constants.ANIMATION_DURATION_MEDIUM);
        view.startAnimation(slideUp);
        view.setVisibility(View.VISIBLE);
    }

    /**
     * Slide down animation
     */
    public static void slideDown(View view) {
        TranslateAnimation slideDown = new TranslateAnimation(
                0, 0, 0, view.getHeight()
        );
        slideDown.setDuration(Constants.ANIMATION_DURATION_MEDIUM);
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(slideDown);
    }
}