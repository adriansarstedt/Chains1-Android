package com.example.adriansarstedt.words2;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

public class ShakeAnimation {


    private View rotateview;
    private RotateAnimation startLarge, endLarge, startSmall, endSmall;
    private Animation middleLarge, middleSmall;


    public ShakeAnimation(Context ctx) {
        startLarge = new RotateAnimation(0f, -10f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        endLarge = new RotateAnimation(10, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        startSmall = new RotateAnimation(0f, -5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        endSmall = new RotateAnimation(5, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        middleLarge = AnimationUtils.loadAnimation(ctx, R.anim.shake_large);
        middleSmall = AnimationUtils.loadAnimation(ctx, R.anim.shake_small);

        startLarge.setDuration(300);
        endLarge.setDuration(300);

        startLarge.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) { rotateview.startAnimation(middleLarge); }
        });

        middleLarge.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) { rotateview.startAnimation(endLarge); }
        });

        startSmall.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) { rotateview.startAnimation(middleSmall); }
        });

        middleSmall.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) { rotateview.startAnimation(endSmall); }
        });
    }

    public void runAnimationLarge(final View view) {
        rotateview = view;
        rotateview.startAnimation(startLarge);
    }

    public void runAnimationSmall(final View view) {
        rotateview = view;
        rotateview.startAnimation(startSmall);
    }
}