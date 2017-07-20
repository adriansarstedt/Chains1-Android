package com.example.adriansarstedt.words2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class GameOverRevised extends AppCompatActivity {

    TextView WellDoneTitle, ScoreTitle, PressToReplayTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_revised);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }


        WellDoneTitle = (TextView)findViewById(R.id.WellDoneTitle);
        ScoreTitle = (TextView)findViewById(R.id.ScoreTitle);
        PressToReplayTitle = (TextView)findViewById(R.id.PressToRetryTitle);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lato-Thin.ttf");
        WellDoneTitle.setTypeface(custom_font);
        ScoreTitle.setTypeface(custom_font);
        PressToReplayTitle.setTypeface(custom_font);

        ScoreTitle.setText("34");
    }

    public void ReturnHome(View view) {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent ReturnHomeIntent = new Intent(this, HomePage.class);
        startActivity(ReturnHomeIntent);
        //overridePendingTransition(R.anim.pull_in_left, R.anim.pull_out_right);
    }

}
