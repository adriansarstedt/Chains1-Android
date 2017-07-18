package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.app.Activity;
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
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameOver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

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

        Typeface custom_font_hairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Thin.ttf");

        TextView GameOverLabel = (TextView) findViewById(R.id.GameOverLabel);
        TextView ScoreLabel = (TextView) findViewById(R.id.ScoreLabel);
        TextView ScoreView = (TextView) findViewById(R.id.ScoreView);
        TextView HighScoreLabel = (TextView) findViewById(R.id.HighScoreLabel);
        TextView HighScoreView = (TextView) findViewById(R.id.HighScoreView);

        GameOverLabel.setTypeface(custom_font_hairline);
        ScoreLabel.setTypeface(custom_font_hairline);
        ScoreView.setTypeface(custom_font_hairline);
        HighScoreLabel.setTypeface(custom_font_hairline);
        HighScoreView.setTypeface(custom_font_hairline);

        SharedPreferences HighScoreManager = this.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        int HighScore = HighScoreManager.getInt(Globals.User+Globals.Difficulty, 0);

        if (Score > HighScore) {
            SharedPreferences.Editor HighScoreEditor = HighScoreManager.edit();
            HighScoreEditor.putInt(Globals.User+Globals.Difficulty, Score);
            HighScoreEditor.apply();
        }

        ScoreView.setText(String.valueOf(Score));
        HighScoreView.setText(String.valueOf(HighScore));

        StartIntroAnimation();
    }

    public void StartIntroAnimation() {
        View ScoreHolder = (View) findViewById(R.id.ScoreHolder), ButtonHolder = (View) findViewById(R.id.ButtonHolder);
        TextView GameOverLabel = (TextView) findViewById(R.id.GameOverLabel);

        ObjectAnimator GameOverLabelAnimator= ObjectAnimator.ofFloat(GameOverLabel, "translationX", 1000, 0);
        GameOverLabelAnimator.setDuration(1000);
        GameOverLabelAnimator.start();

        ObjectAnimator ScoreHolderAnimator= ObjectAnimator.ofFloat(ScoreHolder, "translationX", 1000, 0);
        ScoreHolderAnimator.setDuration(1000);
        ScoreHolderAnimator.start();

        ObjectAnimator ButtonHolderAnimator= ObjectAnimator.ofFloat(ButtonHolder, "translationX", 1000, 0);
        ButtonHolderAnimator.setDuration(1000);
        ButtonHolderAnimator.start();
    }

    public void NewGame(View view) {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent NewGameIntent = new Intent(this, Game.class);
        startActivity(NewGameIntent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.pull_out_right);
    }

    public void ReturnHome(View view) {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent ReturnHomeIntent = new Intent(this, HomePage.class);
        startActivity(ReturnHomeIntent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.pull_out_right);
    }

    public void OpenResearchCenter(View view) {

        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent ResearchCenterIntent = new Intent(this, ResearchCenter.class);
        startActivity(ResearchCenterIntent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.pull_out_right);
    }
}
