package com.example.adriansarstedt.words2;

import android.view.animation.Animation;
import android.view.animation.Transformation;


public class ArcGrowAnimation extends Animation {

    private ArcView arcView;

    private float oldAngle;

    public ArcGrowAnimation(ArcView arcView) {
        this.oldAngle = arcView.getArcAngle();
        this.arcView = arcView;
    }

    public void updateAngle() {
        this.oldAngle = arcView.getArcAngle();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle - ((360+oldAngle) * (interpolatedTime));

        arcView.setArcAngle(angle);
        arcView.requestLayout();
    }
}