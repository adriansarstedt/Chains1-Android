package com.example.adriansarstedt.words2;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PopInDisplay extends RelativeLayout {

    Context ctx;
    ImageView icon;
    TextView message;

    int width;

    Animation shake, focus, slide_in, slide_out;

    public PopInDisplay(Context context) {
        super(context);
        initializeViews(context);
        ctx = context;
    }

    public PopInDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
        ctx = context;
    }

    public PopInDisplay(Context context,
                    AttributeSet attrs,
                    int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
        ctx = context;
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pop_in_display, this);
    }

    @Override
    protected void onFinishInflate() {

        this.icon = (ImageView) findViewById(R.id.pop_in_icon);
        this.message = (TextView) findViewById(R.id.pop_in_message);

        Typeface custom_font_hairline = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Thin.ttf");
        message.setTypeface(custom_font_hairline);

        setupAnimations();


        this.post(new Runnable() {
            @Override
            public void run() {
                setupAnimations();
            }
        });



        super.onFinishInflate();
    }

    public void setupAnimations() {
        shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake_large);
        focus = AnimationUtils.loadAnimation(getContext(), R.anim.grow_shrink);

        width = this.getWidth()-20;
        this.setX(-(width+20));

        slide_in = new TranslateAnimation(0, width, 300, 300);
        slide_in.setDuration(800);
        slide_in.setFillAfter(true);

        slide_out = new TranslateAnimation(width, 0, 300, 300);
        slide_out.setDuration(800);
        slide_out.setFillAfter(true);

        slide_in.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation) {
                icon.startAnimation(shake);
                message.startAnimation(focus);
            }
        });

        shake.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation) {
                PopInDisplay.super.startAnimation(slide_out);
            }
        });
    }

    public void newEggAnimation() {
        this.icon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.egg2));
        this.startAnimation(slide_in);
    }

    public void newDiscoveryAnimation() {
        this.icon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.researchcenterimagesmall));
        this.startAnimation(slide_in);
    }
}