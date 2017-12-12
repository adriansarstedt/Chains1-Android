package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Random;

public class HelpPopup extends FragmentActivity {

    MyPagerAdapter adapter;
    ViewPager pager;
    Button GotItButton;
    ColorDrawable Background;

    int ButtonHeight, ButtonWidth;
    ScaleAnimation growAnim, shrinkAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_popup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }

        Background = new ColorDrawable(Color.parseColor("#55FF0033"));
        findViewById(R.id.activity_help_popup).setBackground(Background);

        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        GotItButton = (Button) findViewById(R.id.GotItButton);

        pager.setAdapter(adapter);

        if (getIntent().getBooleanExtra("FromResearchCenter", false)) {
            pager.setCurrentItem(3);
        }

        runIntroAnimations();
    }

    private void runIntroAnimations() {

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(Background, "alpha", 0, 255);
        bgAnim.setDuration(1000);
        bgAnim.start();

        Animation SlideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pull_in_right);
        pager.startAnimation(SlideIn);

        growAnim = new ScaleAnimation(0.98f, 1.02f, 0.98f, 1.02f,
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot points
                Animation.RELATIVE_TO_SELF, 0.5f);
        shrinkAnim = new ScaleAnimation(1.02f, 0.98f, 1.02f, 0.98f,
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot points
                Animation.RELATIVE_TO_SELF, 0.5f);

        growAnim.setDuration(1000);
        shrinkAnim.setDuration(1000);

        growAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                GotItButton.setAnimation(shrinkAnim);
                shrinkAnim.start();
            }
        });
        shrinkAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                GotItButton.setAnimation(growAnim);
                growAnim.start();
            }
        });

        GotItButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                GotItButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ButtonHeight = GotItButton.getHeight();
                ButtonWidth = GotItButton.getWidth();

                GotItButton.setScaleX(0); GotItButton.setScaleY(0);
                GotItButton.setPivotY(Math.round(ButtonHeight/2));
                GotItButton.setPivotX(Math.round(ButtonWidth/2));
                GotItButton.animate().setDuration(500).
                        scaleY(1).scaleX(1).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        GotItButton.setAnimation(growAnim);
                        growAnim.start();
                    }
                });
            }
        });
    }

    private void runExitAnimations() {

        final Animation SlideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pull_out_right);

        pager.startAnimation(SlideOut);

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(Background, "alpha", 255, 0);
        bgAnim.setDuration(300);
        bgAnim.start();

        GotItButton.setPivotY(Math.round(GotItButton.getHeight()/2));
        GotItButton.setPivotX(Math.round(GotItButton.getWidth()/2));
        GotItButton.animate().setDuration(500).
                scaleY(0).scaleX(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {

            switch(pos) {
                case 0: return HelpFragment1.newInstance("EVERY GAME STARTS WITH ONE LETTER", "", "E", "YOU NEED TO THINK OF AN ANIMAL THAT STARTS WITH THAT LETTER");
                case 1: return HelpFragment2.newInstance("LET'S SAY YOU CHOOSE ECHIDNA", "E", "CHIDNA", "THE WORD CHAIN CONTINUES! NOW YOU NEED TO THINK OF AN ANIMAL STARTING WITH A");
                case 2: return HelpFragment3.newInstance("MAKE SURE YOUR TIME DOESN'T RUN OUT", "A", "");
                case 3: return HelpFragment4.newInstance();
                case 4: return HelpFragment5.newInstance();
                case 5: return HelpFragment6.newInstance();
                default: return HelpFragment1.newInstance("a", "b", "c", "d");
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    @Override
    public void onBackPressed() {
        runExitAnimations();
    }

    public void Close(View view) {
        runExitAnimations();
    }
}
