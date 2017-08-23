package com.example.adriansarstedt.words2;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ArcShrinkAnimation extends Animation {

    private ArcView arcView;

    private float oldAngle;

    public ArcShrinkAnimation(ArcView arcView) {
        arcView.setArcAngle(-360);
        this.oldAngle = arcView.getArcAngle();
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