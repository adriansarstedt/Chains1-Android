package com.example.adriansarstedt.words2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class GameOverRevised extends AppCompatActivity {

    TextView WellDoneTitle, PressToReplayTitle, ScoreLabel, HighScoreLabel, EggLabel;
    Handler FocusHandler = new Handler();
    Animation Focus;
    ViewAnimator AchievementViewAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_revised);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }

        int Score = getIntent().getIntExtra("score", 0);

        String NewlyDiscovered = getIntent().getStringExtra("newlyDiscoveredAnimals");
        String PreviouslyDiscovered = PreferenceManager.getDefaultSharedPreferences(this).getString("DiscoveredAnimals", "");

        if (!PreviouslyDiscovered.equals("") && (!NewlyDiscovered.equals(""))) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("DiscoveredAnimals", PreviouslyDiscovered+"-"+NewlyDiscovered).commit();
        } else if (PreviouslyDiscovered.equals("")) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("DiscoveredAnimals", NewlyDiscovered).commit();
        }

        SharedPreferences HighScoreManager = this.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        int HighScore = HighScoreManager.getInt(Globals.User+Globals.Difficulty, 0);

        WellDoneTitle = (TextView)findViewById(R.id.WellDoneTitle);
        ScoreLabel = (TextView)findViewById(R.id.ScoreLabel);
        HighScoreLabel = (TextView)findViewById(R.id.HighScoreLabel);
        EggLabel = (TextView) findViewById(R.id.EggLabel);
        PressToReplayTitle = (TextView)findViewById(R.id.PressToRetryTitle);
        AchievementViewAnimator = (ViewAnimator) findViewById(R.id.AchievementViewAnimator);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lato-Thin.ttf");
        WellDoneTitle.setTypeface(custom_font);
        PressToReplayTitle.setTypeface(custom_font);
        ScoreLabel.setTypeface(custom_font);
        HighScoreLabel.setTypeface(custom_font);
        EggLabel.setTypeface(custom_font);

        ScoreLabel.setText(String.valueOf(Score));
        HighScoreLabel.setText(String.valueOf(HighScore));

        Focus = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.grow_shrink);
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left); // load an animation
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        AchievementViewAnimator.setInAnimation(in); // set in Animation for ViewAnimator
        AchievementViewAnimator.setOutAnimation(out);

        FocusHandler = new Handler();
        FocusHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                PressToReplayTitle.startAnimation(Focus);
                AchievementViewAnimator.showPrevious();

                FocusHandler.postDelayed(this, 2000);
            }
        }, 1000);
    }

    public void ReturnHome(View view) {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent ReturnHomeIntent = new Intent(this, HomePage.class);
        startActivity(ReturnHomeIntent);
    }

    public void NewGame(View view) {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent NewGameIntent = new Intent(this, Game.class);
        startActivity(NewGameIntent);
    }

}
