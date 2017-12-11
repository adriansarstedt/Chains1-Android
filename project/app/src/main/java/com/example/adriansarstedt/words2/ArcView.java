package com.example.adriansarstedt.words2;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ArcView extends View {

    private final Paint mPaint;
    private RectF mRect;
    private float arcAngle;

    public ArcView(Context context, AttributeSet attrs) {

        super(context, attrs);

        arcAngle = -360;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(40);
        mPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthPixels = View.MeasureSpec.getSize( widthMeasureSpec );
        int heightPixels = View.MeasureSpec.getSize( widthMeasureSpec );
        setCanvasRect(widthPixels, heightPixels);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRect != null) {
            canvas.drawArc(mRect, 270, arcAngle, false, mPaint);
        }
    }

    public void setCanvasRect(int width, int height) {
        int paddingOffset = 25+Math.round(Globals.dipToPixels(getContext(), 10));
        mRect = new RectF(paddingOffset, paddingOffset, width-paddingOffset, height-paddingOffset);
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
    }
}