package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
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
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameOverRevised extends AppCompatActivity {

    TextView WellDoneTitle, PressToReplayTitle, ScoreLabel, HighScoreLabel, EggLabel;
    Handler FocusHandler = new Handler();
    Animation Focus;
    ViewAnimator AchievementViewAnimator;
    ArrayList<String> NewlyDiscoveredAnimalsList;
    GridView AnimalDisplayRow1, AnimalDisplayRow2;
    CustomGridAdapter AnimalAdapterRow1, AnimalAdapterRow2;

    boolean adjusted1 = false, adjusted2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_revised);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }

        int Score = getIntent().getIntExtra("score", 0);
        int NewEggCount = getIntent().getIntExtra("NewEggCount", 0);

        String NewlyDiscovered = getIntent().getStringExtra("newlyDiscoveredAnimals");
        NewlyDiscoveredAnimalsList = new ArrayList<String>(Arrays.asList(NewlyDiscovered.split("-")));
        String PreviouslyDiscovered = PreferenceManager.getDefaultSharedPreferences(this).getString("DiscoveredAnimals", "");

        if (!PreviouslyDiscovered.equals("") && (!NewlyDiscovered.equals(""))) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("DiscoveredAnimals", PreviouslyDiscovered+"-"+NewlyDiscovered).apply();
        } else if (PreviouslyDiscovered.equals("")) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("DiscoveredAnimals", NewlyDiscovered).apply();
        }

        SharedPreferences HighScoreManager = this.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        int HighScore = HighScoreManager.getInt(Globals.User+Globals.Difficulty, 0);

        if (Score > HighScore) {
            SharedPreferences.Editor HighScoreEditor = HighScoreManager.edit();
            HighScoreEditor.putInt(Globals.User+Globals.Difficulty, Score);
            HighScoreEditor.apply();
        }

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
        EggLabel.setText(String.valueOf(NewEggCount));

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

        int EggCount = PreferenceManager.getDefaultSharedPreferences(this).getInt("EggCount", 0);
        EggCount = EggCount + NewEggCount;
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("EggCount", EggCount).apply();

        final ArrayList<String> AnimalListRow1 = new ArrayList<>(), AnimalListRow2 = new ArrayList<>();

        if (NewlyDiscoveredAnimalsList.size()>5) {
            AnimalListRow1.addAll(NewlyDiscoveredAnimalsList.subList(0,5));

            if (NewlyDiscoveredAnimalsList.size()>8) {
                AnimalListRow2.addAll(NewlyDiscoveredAnimalsList.subList(5, 8));
            } else {
                AnimalListRow2.addAll(NewlyDiscoveredAnimalsList.subList(5, NewlyDiscoveredAnimalsList.size()-1));
            }
            AnimalListRow2.add(AnimalListRow2.size(), "researchcenter");
        } else {
            NewlyDiscoveredAnimalsList.add(NewlyDiscoveredAnimalsList.size(), "researchcenter");
            AnimalListRow1.addAll(NewlyDiscoveredAnimalsList);
        }

        AnimalDisplayRow1 = (GridView) findViewById(R.id.DiscoveredAnimalDisplay1);
        AnimalDisplayRow2 = (GridView) findViewById(R.id.DiscoveredAnimalDisplay2);
        AnimalAdapterRow1 = new CustomGridAdapter(GameOverRevised.this, AnimalListRow1);
        AnimalAdapterRow2 = new CustomGridAdapter(GameOverRevised.this, AnimalListRow2);
        AnimalDisplayRow1.setAdapter(AnimalAdapterRow1);
        AnimalDisplayRow2.setAdapter(AnimalAdapterRow2);

        AnimalDisplayRow1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!adjusted1) {
                    int paddingHorizontal = AnimalDisplayRow1.getPaddingLeft();
                    int paddingVertical = AnimalDisplayRow1.getPaddingTop();
                    int spacingHorizontal = AnimalDisplayRow1.getHorizontalSpacing();
                    int width = AnimalDisplayRow1.getColumnWidth();
                    int numCollums1 = AnimalListRow1.size();

                    ViewGroup.LayoutParams layoutParams = AnimalDisplayRow1.getLayoutParams();
                    layoutParams.width = 2*paddingHorizontal+numCollums1*width+(numCollums1-1)*spacingHorizontal;
                    layoutParams.height = 2*paddingVertical+width;
                    AnimalDisplayRow1.setLayoutParams(layoutParams);

                    adjusted1 = true;
                }
            }
        });

        AnimalDisplayRow2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!adjusted2) {
                    int paddingHorizontal = AnimalDisplayRow2.getPaddingLeft();
                    int paddingVertical = AnimalDisplayRow2.getPaddingTop();
                    int spacingHorizontal = AnimalDisplayRow2.getHorizontalSpacing();
                    int width = AnimalDisplayRow2.getColumnWidth();
                    int numCollums2 = AnimalListRow2.size();

                    ViewGroup.LayoutParams layoutParams = AnimalDisplayRow2.getLayoutParams();
                    layoutParams.width = 2*paddingHorizontal+numCollums2*width+(numCollums2-1)*spacingHorizontal;
                    layoutParams.height = 2*paddingVertical+width;
                    AnimalDisplayRow2.setLayoutParams(layoutParams);

                    adjusted2 = true;
                }
            }
        });

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
