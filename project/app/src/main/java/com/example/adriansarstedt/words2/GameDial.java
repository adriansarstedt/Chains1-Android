package com.example.adriansarstedt.words2;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameDial extends LinearLayout {

    TextView tv;
    ArcView av;
    ImageView iv;
    View bg, fc, md;
    Context ctx;
    int score = 0;
    boolean new_species_discovered = false;

    int shrinkDuration = 10000, growDuration = 1000;

    ArcShrinkAnimation asa, ada;
    ArcGrowAnimation aga;
    ValueAnimator SaturationAnimator;
    Animation flipStart, flipEnd, imageFlipStart, imageFlipEnd, focus, shake_small, shake_large;

    Bitmap dr, drOriginal;

    Handler messageHandler;

    Game game;
    EditText et;
    ImageButton ib;


    public GameDial(Context context) {
        super(context);
        initializeViews(context);
        ctx = context;
    }

    public GameDial(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
        ctx = context;
    }

    public GameDial(Context context,
                           AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
        ctx = context;
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

    public void setGame(Game game) {
        this.game = game;
        this.et = game.InputTextEdit;
        this.ib = game.HelpButton;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void reset() {
        score = 0;
        tv.setText(String.valueOf(score));
        iv.setImageDrawable(null);
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

    public void resetTimer() {
        aga.updateAngle();
        av.startAnimation(aga);
    }

    public void regenerate(String newAnimal, boolean newDiscovery) {

        score += 1;

        drOriginal = BitmapFactory.decodeResource(ctx.getResources(),
                ctx.getResources().getIdentifier(newAnimal.toLowerCase() + "imagesmall", "drawable",
                        getContext().getPackageName()));

        if (newDiscovery) {
            discovery();
        } else {
            updateDisplay(String.valueOf(score), drOriginal, 80);
        }

        aga.updateAngle();
        av.startAnimation(aga);

    }

    public void discovery() {

        dr = Globals.toGrayscale(drOriginal, 0);
        new_species_discovered = true;
        this.startAnimation(focus);
        updateDisplay(String.valueOf(score), dr, 80);

    }

    public void displayMessage(final String message, final Bitmap tmpDr, boolean highlight) {

        Bitmap tOldBitmap = null;
        if (iv.getBackground() != null) {
            tOldBitmap = ((BitmapDrawable)iv.getBackground()).getBitmap();
        }
        final Bitmap oldBitmap = tOldBitmap;

        if (et != null) {
            if (highlight) {
                et.selectAll();
            }
            et.startAnimation(shake_small);
        }

        if (ib != null) {
            ib.startAnimation(shake_large);
        }

        updateDisplay(message, tmpDr, 40);

        messageHandler.removeCallbacksAndMessages(null);
        messageHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (tmpDr != null) {
                    updateDisplay(String.valueOf(score), oldBitmap, 80);
                } else {
                    updateDisplay(String.valueOf(score), null, 80);
                }


                messageHandler.removeCallbacks(this);
            }
        }, 2000);

        md.startAnimation(focus);
    }

    public void updateDisplay(final String newText, final Bitmap newImage, final int newTextSize) {
        flipStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (newImage != null) {
                    iv.startAnimation(imageFlipStart);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv.setTextSize(newTextSize);
                tv.setText(newText);
                tv.startAnimation(flipEnd);

                if (newImage != null) {
                    if (new_species_discovered) {

                        SaturationAnimator.removeAllUpdateListeners();
                        SaturationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (SaturationAnimator.getAnimatedFraction()%0.1<0.02) {
                                    dr = Globals.toGrayscale(drOriginal, SaturationAnimator.getAnimatedFraction());
                                    iv.setImageBitmap(dr);
                                } if (SaturationAnimator.getAnimatedFraction() == 1) {
                                    new_species_discovered = false;
                                }
                            }
                        });
                        SaturationAnimator.start();
                    }
                    iv.setImageBitmap(newImage);
                    iv.startAnimation(imageFlipEnd);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        tv.startAnimation(flipStart);
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

        SaturationAnimator = ValueAnimator.ofFloat(0f, 1f);
        SaturationAnimator.setDuration(2000);

        messageHandler = new Handler();
        imageFlipStart = AnimationUtils.loadAnimation(getContext(),
                R.anim.flip_start);
        imageFlipEnd = AnimationUtils.loadAnimation(getContext(),
                R.anim.flip_end);
        flipStart = AnimationUtils.loadAnimation(getContext(),
                R.anim.flip_start);
        flipEnd = AnimationUtils.loadAnimation(getContext(),
                R.anim.flip_end);
        focus = AnimationUtils.loadAnimation(getContext(),
                R.anim.grow_shrink);
        shake_small = AnimationUtils.loadAnimation(getContext(), R.anim.shake_small);
        shake_large = AnimationUtils.loadAnimation(getContext(), R.anim.shake_large);
    }
}