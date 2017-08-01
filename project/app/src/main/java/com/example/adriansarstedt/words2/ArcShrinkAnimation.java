package com.example.adriansarstedt.words2;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ArcShrinkAnimation extends Animation {

    private ArcView arcView;

    private float oldAngle;
    private float newAngle;

    public ArcShrinkAnimation(ArcView arcView, int newAngle) {
        this.oldAngle = arcView.getArcAngle();
        this.newAngle = newAngle;
        this.arcView = arcView;
    }

    public void updateAngle() {
        this.oldAngle = arcView.getArcAngle();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = (oldAngle) * (1-interpolatedTime);

        arcView.setArcAngle(angle);
        arcView.requestLayout();
    }
}