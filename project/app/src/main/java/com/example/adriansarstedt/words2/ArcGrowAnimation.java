package com.example.adriansarstedt.words2;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by adriansarstedt on 25/12/2016.
 */

public class ArcGrowAnimation extends Animation {

    private ArcView arcView;

    private float oldAngle;

    public ArcGrowAnimation(ArcView arcView) {
        this.oldAngle = arcView.getArcAngle();
        this.arcView = arcView;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle - ((360+oldAngle) * (interpolatedTime));

        arcView.setArcAngle(angle);
        arcView.requestLayout();
    }
}