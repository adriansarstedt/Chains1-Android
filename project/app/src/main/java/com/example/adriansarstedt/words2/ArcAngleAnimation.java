package com.example.adriansarstedt.words2;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by adriansarstedt on 25/12/2016.
 */

public class ArcAngleAnimation extends Animation {

    private ArcView arcView;

    private float oldAngle;
    private float newAngle;

    public ArcAngleAnimation(ArcView arcView, int newAngle) {
        this.oldAngle = arcView.getArcAngle();
        this.newAngle = newAngle;
        this.arcView = arcView;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = -((newAngle) * (1-interpolatedTime));

        arcView.setArcAngle(angle);
        arcView.requestLayout();
    }
}