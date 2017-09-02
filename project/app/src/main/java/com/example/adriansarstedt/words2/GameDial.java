package com.example.adriansarstedt.words2;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameDial extends LinearLayout {

    TextView tv;
    ArcView av;
    ImageView iv;
    View bg, fc, md;

    int shrinkDuration = 10000, growDuration = 1000;

    ArcShrinkAnimation asa, ada;
    ArcGrowAnimation aga;
    ValueAnimator SaturationAnimator;
    Animation messageFlipStart, messageFlipEnd, focus, shake;

    Bitmap dr, drOriginal;

    Handler messageHandler;


    public GameDial(Context context) {
        super(context);
        initializeViews(context);
    }

    public GameDial(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public GameDial(Context context,
                           AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.game_dial, this);
    }


    @Override
    protected void onFinishInflate() {

        tv = (TextView) this.findViewById(R.id.tv);
        av = (ArcView) this.findViewById(R.id.av);
        iv = (ImageView) this.findViewById(R.id.iv);
        bg = this.findViewById(R.id.bg);
        fc = this.findViewById(R.id.fc);
        md = this.findViewById(R.id.gd);

        Typeface custom_font_hairline = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Thin.ttf");

        tv.setTypeface(custom_font_hairline);

        setupAnimations();

        super.onFinishInflate();
    }

    public void startTimer(final Runnable endAction) {
        asa.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                endAction.run();
            }
        });

        av.startAnimation(asa);
    }

    public void regenerate(int newScore, String newAnimal) {
    }

    public void discovery(int newScore, String newAnimal) {
    }

    public void displayMessage(final String message, EditText highlightText, Button helpButton, Bitmap tmpDr) {

        messageFlipStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                tv.setTextSize(40);
                tv.setText(message);
                tv.startAnimation(messageFlipEnd);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        tv.startAnimation(messageFlipStart);

        if (highlightText != null) {
            highlightText.selectAll();
            highlightText.startAnimation(shake);
        }

        if (helpButton != null) {
            helpButton.startAnimation(shake);
        }

        messageHandler.removeCallbacksAndMessages(null);
        messageHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageFlipStart.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tv.setTextSize(80);
                        tv.setText(String.valueOf(4));
                        tv.startAnimation(messageFlipEnd);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                tv.startAnimation(messageFlipStart);
                messageHandler.removeCallbacks(this);
            }
        }, 2000);

        md.startAnimation(focus);

        if (tmpDr != null) {
            //displayMessageWithImage
        }
    }

    public void setShrinkTime(int t) {
        shrinkDuration = t;
    }

    public void setGrowTime(int t) {
        growDuration = t;
    }

    public void setupAnimations() {
        asa = new ArcShrinkAnimation(av);
        asa.setDuration(shrinkDuration);

        SaturationAnimator = ValueAnimator.ofFloat(0f, 1f);
        SaturationAnimator.setDuration(2000);
        SaturationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dr = Globals.toGrayscale(drOriginal, SaturationAnimator.getAnimatedFraction());
                iv.setImageBitmap(dr);
            }
        });

        aga = new ArcGrowAnimation(av);
        aga.setDuration(growDuration);

        aga.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) { av.startAnimation(asa); }
        });

        ada = new ArcShrinkAnimation(av);
        ada.setDuration(1000);

        ada.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){
                Animation Focus = AnimationUtils.loadAnimation(getContext(),
                        R.anim.grow_shrink);
                md.startAnimation(Focus);
            }

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                SaturationAnimator.start();
                aga.updateAngle();
                av.startAnimation(aga);
            }
        });

        messageHandler = new Handler();
        messageFlipStart = AnimationUtils.loadAnimation(getContext(),
                R.anim.flip_start);
        messageFlipEnd = AnimationUtils.loadAnimation(getContext(),
                R.anim.flip_end);
        focus = AnimationUtils.loadAnimation(getContext(),
                R.anim.grow_shrink);
        shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
    }
}