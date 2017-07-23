package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class HomePage extends AppCompatActivity {

    ImageButton PlayButton;
    TextView AppTitle;
    private AdView HomeAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }

        AppTitle = (TextView)findViewById(R.id.AppTitle);
        TextView EggCountDisplay = (TextView)findViewById(R.id.EggCountDisplay);
        PlayButton = (ImageButton)findViewById(R.id.PlayButton);
        HomeAdView = (AdView) findViewById(R.id.adViewHome);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");
        AdRequest adRequest = new AdRequest.Builder().build();
        HomeAdView.loadAd(adRequest);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lato-Thin.ttf");
        AppTitle.setTypeface(custom_font);
        EggCountDisplay.setTypeface(custom_font);

        int EggCount = PreferenceManager.getDefaultSharedPreferences(this).getInt("EggCount", 0);
        EggCountDisplay.setText(String.valueOf(EggCount));

        /*
        final Handler animationStartHandler = new Handler();
        animationStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SetupPlayButtonAnimation();
                animationStartHandler.removeCallbacks(this);
            }
        }, 500);
        */
    }

    public void StartAnimations() {

        View ActionBar = findViewById(R.id.ActionBar);
        View OptionsBar = findViewById(R.id.OptionsBar);

        ObjectAnimator titleAnimator= ObjectAnimator.ofFloat(AppTitle, "translationX", 1000, 0);
        titleAnimator.setDuration(1000);
        titleAnimator.start();

        ObjectAnimator OptionsAnimator= ObjectAnimator.ofFloat(OptionsBar, "translationX", 1000, 0);
        OptionsAnimator.setDuration(1000);
        OptionsAnimator.start();

        ObjectAnimator ActionBarAnimator= ObjectAnimator.ofFloat(ActionBar, "translationY", -500, 0);
        ActionBarAnimator.setDuration(1000);
        ActionBarAnimator.start();

        ObjectAnimator AddViewAnimator= ObjectAnimator.ofFloat(HomeAdView, "translationY", 500, 0);
        AddViewAnimator.setDuration(1000);
        AddViewAnimator.start();

        final ScaleAnimation startGrowAnim = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        startGrowAnim.setDuration(1000);

        PlayButton.setAnimation(startGrowAnim);
        startGrowAnim.start();

        startGrowAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                SetupPlayButtonAnimation();
            }
        });
    }

    public void SetupPlayButtonAnimation() {
        final ScaleAnimation growAnim = new ScaleAnimation(1.0f, 0.85f, 1.0f, 0.85f,
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot points
                Animation.RELATIVE_TO_SELF, 0.5f);
        final ScaleAnimation shrinkAnim = new ScaleAnimation(0.85f, 1.0f, 0.85f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot points
                Animation.RELATIVE_TO_SELF, 0.5f);

        growAnim.setDuration(2000);
        shrinkAnim.setDuration(2000);

        PlayButton.setAnimation(growAnim);
        growAnim.start();

        growAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                PlayButton.setAnimation(shrinkAnim);
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
                PlayButton.setAnimation(growAnim);
                growAnim.start();
            }
        });
    }

    public void OptionsButtonPressed(View view) {
        Intent OptionsIntent = new Intent(this, Options.class);
        startActivity(OptionsIntent);
        overridePendingTransition(R.anim.pull_in_bottom, R.anim.pull_out_top);
    }

    public void NewGame(View view) {

        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent NewGameIntent = new Intent(this, Game.class);
        startActivity(NewGameIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.pull_out_left);
    }

    public void OpenResearchCenter(View view) {

        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent ResearchCenterIntent = new Intent(this, ResearchCenter.class);
        startActivity(ResearchCenterIntent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.pull_out_right);
    }
}
