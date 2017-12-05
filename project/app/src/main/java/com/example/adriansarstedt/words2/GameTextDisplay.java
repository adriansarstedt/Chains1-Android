package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class GameTextDisplay extends LinearLayout {

    private TextView tv1, tv2;
    View c;
    ArrayList<Handler> HandlerArray = new ArrayList<Handler>();

    public GameTextDisplay(Context context) {
        super(context);
        initializeViews(context);
    }

    public GameTextDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public GameTextDisplay(Context context,
                       AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.game_text_display, this);
    }

    @Override
    protected void onFinishInflate() {

        tv1 = (TextView) this.findViewById(R.id.t1);
        tv2 = (TextView) this.findViewById(R.id.t2);
        c = (View) this.findViewById(R.id.TextContainer);

        Typeface custom_font_hairline = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Thin.ttf");

        tv1.setTypeface(custom_font_hairline);
        tv2.setTypeface(custom_font_hairline);

        super.onFinishInflate();
    }

    public void animateIn(final String leftPortion, final String rightPortion, int timeDelay, final GameDial gd, final boolean isMessage) {

        final Handler animationStartHandler = new Handler();
        animationStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                tv1.setText(leftPortion);
                tv2.setText(rightPortion);

                tv1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        tv1.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int translationA = (c.getWidth() - tv2.getWidth()) / 2;
                        int translationB = (c.getWidth() - tv1.getWidth()) / 2;

                        ObjectAnimator outAnimation = ObjectAnimator.ofFloat(tv1, "X", translationB, translationB - tv2.getWidth() / 2);
                        outAnimation.setDuration(600);
                        outAnimation.start();

                        ObjectAnimator inAnimation = ObjectAnimator.ofFloat(tv2, "X", 3000, translationA + tv1.getWidth() / 2);
                        inAnimation.setDuration(800);
                        inAnimation.start();
                    }
                });

                if (gd != null && !isMessage) {
                    gd.regenerate(leftPortion+rightPortion, true);
                }

                if (isMessage) {
                    activateSmallText();
                }

                animationStartHandler.removeCallbacks(this);
            }
        }, timeDelay);
        HandlerArray.add(animationStartHandler);
    }

    public void animateOut(final String leftPortion, final String rightPortion, int timeDelay, final GameDial gd) {

        final Handler animationStartHandler = new Handler();
        animationStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv1.setText(leftPortion);
                tv2.setText(rightPortion);

                tv1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    tv1.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    int translationA = (c.getWidth() - tv1.getWidth() - tv2.getWidth()) / 2;
                    int translationB = translationA+tv1.getWidth();

                    ObjectAnimator outAnimation = ObjectAnimator.ofFloat(tv1, "X", translationA, -1000);
                    outAnimation.setDuration(1000);
                    outAnimation.start();

                    ObjectAnimator inAnimation = ObjectAnimator.ofFloat(tv2, "X", translationB, (c.getWidth()-tv2.getWidth()) / 2);
                    inAnimation.setDuration(500);
                    inAnimation.start();
                }
                });

                //if (gd != null) {
                //    gd.regenerate(leftPortion+rightPortion);
                //}

                animationStartHandler.removeCallbacks(this);
            }
        }, timeDelay);
        HandlerArray.add(animationStartHandler);
    }

    public void resetView() {
        tv1.setText("");
        tv2.setText("");
        tv1.setTranslationX(3000);
        tv2.setTranslationX(3000);
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);

        for (int x=0; x<HandlerArray.size(); x++) {
            HandlerArray.get(x).removeCallbacksAndMessages(null);
        }
    }

    public void activateSmallText() {
        tv2.setTextSize(20);
    }

    public void setTextA( String str ) {
        tv1.setText(str);
    }

    public void setTextB( String str ) {
        tv2.setText(str);
    }
}