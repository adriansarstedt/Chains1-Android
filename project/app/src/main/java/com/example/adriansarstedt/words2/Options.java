package com.example.adriansarstedt.words2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Options extends AppCompatActivity {

    AdView OptionsAdView;
    String Difficulty, Sound;
    ImageButton DifficultyButton, SoundButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#FF0033"));
        }

        OptionsAdView = (AdView) findViewById(R.id.adViewOptions);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");
        AdRequest adRequest = new AdRequest.Builder().build();
        OptionsAdView.loadAd(adRequest);

        TextView OptionsTitle = (TextView) findViewById(R.id.OptionsTitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lato-Thin.ttf");
        OptionsTitle.setTypeface(custom_font);

        Difficulty = PreferenceManager.getDefaultSharedPreferences(this).getString("Difficulty", "Slow");

        DifficultyButton = (ImageButton) findViewById(R.id.DifficultyButton);
        if (Difficulty.equals("Fast")) {
            DifficultyButton.setBackgroundResource(R.drawable.hare_icon);
        } else {
            DifficultyButton.setBackgroundResource(R.drawable.snail_icon);
        }

        Sound = PreferenceManager.getDefaultSharedPreferences(this).getString("Sound", "On");

        SoundButton = (ImageButton) findViewById(R.id.SoundButton);
        if (Sound.equals("On")) {
            SoundButton.setBackgroundResource(R.drawable.sound_on_icon);
        } else {
            SoundButton.setBackgroundResource(R.drawable.sound_off_icon);
        }
    }

    public void ReturnHome(View view) {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent ReturnHomeIntent = new Intent(this, HomePage.class);
        startActivity(ReturnHomeIntent);
        overridePendingTransition(R.anim.pull_in_top, R.anim.pull_out_bottom);
    }

    public void DificultyButtonPressed(View view) {

        Difficulty = PreferenceManager.getDefaultSharedPreferences(this).getString("Difficulty", "Slow");

        final Animation FlipStart = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flip_start);
        final Animation FlipEnd = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flip_end);

        FlipStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if (Difficulty.equals("Fast")) {
                    DifficultyButton.setBackgroundResource(R.drawable.snail_icon);
                } else {
                    DifficultyButton.setBackgroundResource(R.drawable.hare_icon);
                }
                DifficultyButton.startAnimation(FlipEnd);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        DifficultyButton.startAnimation(FlipStart);

        if (Difficulty.equals("Fast")) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Difficulty", "Slow").apply();
        } else {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Difficulty", "Fast").apply();
        }

    }

    public void SoundButtonPressed(View view) {
        Sound = PreferenceManager.getDefaultSharedPreferences(this).getString("Sound", "On");

        if (Sound.equals("On")) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Sound", "Off").apply();
            SoundButton.setBackgroundResource(R.drawable.sound_off_icon);
        } else {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Sound", "On").apply();
            SoundButton.setBackgroundResource(R.drawable.sound_on_icon);
        }
    }

    public void ResetButtonPressed(View view) {
        Intent AlertPopupIntent = new Intent(this, AlertPopup.class);
        AlertPopupIntent.putExtra("Title", "Are you sure?");
        AlertPopupIntent.putExtra("Description", "You will lose all your discoveries, high scores and eggs!");
        startActivity(AlertPopupIntent);
        overridePendingTransition(0, 0);
    }

    public void CreditsButtonPressed(View view) {
        Intent AlertPopupIntent = new Intent(this, AlertPopup.class);
        AlertPopupIntent.putExtra("Title", "Credits");
        AlertPopupIntent.putExtra("Description", "This game was designed and created by Adrian Sarstedt, including graphics, sound and game mechanics. See more projects on my website!\n");
        AlertPopupIntent.putExtra("Type", "adriansarstedt");
        startActivity(AlertPopupIntent);
        overridePendingTransition(0, 0);
    }
}
