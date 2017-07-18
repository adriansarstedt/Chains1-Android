package com.example.adriansarstedt.words2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ArcView extends View {

    private final Paint mPaint;
    private final RectF mRect;
    private float arcAngle;

    public ArcView(Context context, AttributeSet attrs) {

        super(context, attrs);

        arcAngle = 360;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(45);
        mPaint.setColor(Color.WHITE);
        mRect = new RectF(25, 25, 675, 675);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRect, 270, arcAngle, false, mPaint);
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
    }

}