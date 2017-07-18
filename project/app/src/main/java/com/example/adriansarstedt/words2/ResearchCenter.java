package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.content.Intent;
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
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ResearchCenter extends AppCompatActivity {

    GridView MainGrid;
    View ProgressBar, BottomBar;
    TextView DiscoveredCountDisplay, ResearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_center);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }

        DiscoveredCountDisplay = (TextView) findViewById(R.id.DiscoveredCountDisplay);
        ResearchText = (TextView) findViewById(R.id.ResearchText);
        Typeface custom_font_hairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Thin.ttf");
        DiscoveredCountDisplay.setTypeface(custom_font_hairline);

        ProgressBar = (View) findViewById(R.id.progressBar);
        BottomBar = (View) findViewById(R.id.BottomBar);

        MainGrid = (GridView) findViewById(R.id.MainGrid);
        MainGrid.setOnScrollListener(new AbsListView.OnScrollListener(){
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ResearchText.setText(Character.toString(Globals.Animals.get(firstVisibleItem).charAt(0)).toUpperCase());
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });

        final Handler gameStartHandler = new Handler();
        gameStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainGrid.setAdapter(new CustomGridAdapter(ResearchCenter.this, Globals.Animals));
                MainGrid.setFriction(ViewConfiguration.getScrollFriction() * 7);
                System.out.print("test0");
                MainGrid.setOnItemClickListener(TileClickListener);
                gameStartHandler.removeCallbacks(this);
            }
        }, 1200);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        runEntryAnimations();
        super.onWindowFocusChanged(hasFocus);
    }

    private AdapterView.OnItemClickListener TileClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            System.out.println(position);

            int[] screenLocation = new int[2];
            view.getLocationOnScreen(screenLocation);

            Intent InfoIntent = new Intent(ResearchCenter.this, EntryInfo.class);
            InfoIntent.
                    putExtra("Info" + ".position", position).
                    putExtra("Info" + ".left", screenLocation[0]).
                    putExtra("Info" + ".top", screenLocation[1]).
                    putExtra("Info" + ".width", view.getWidth()).
                    putExtra("Info" + ".height", view.getHeight());
            startActivity(InfoIntent);
            overridePendingTransition(0, 0);
        }
    };

    public void runEntryAnimations() {

        String DiscoveredCompressed = PreferenceManager.getDefaultSharedPreferences(this).getString("DiscoveredAnimals", "");
        ArrayList<String> DiscoveredList = new ArrayList<String>(Arrays.asList(DiscoveredCompressed.split("-")));
        float DiscoveredCount = DiscoveredList.size(), TotalCount = Globals.Animals.size();
        float PercentageDiscovered = DiscoveredCount/TotalCount;

        //DiscoveredCountDisplay.setText(String.valueOf(Math.round(DiscoveredCount)) + " Species Discovered");

        ResizeWidthAnimation ProgressBarAnimation = new ResizeWidthAnimation(ProgressBar,
                Math.round(findViewById(R.id.progressBarHolder).getWidth()*PercentageDiscovered));
        ProgressBarAnimation.setDuration(1000);
        ProgressBar.startAnimation(ProgressBarAnimation);
    }

    public void ReturnHome(View view) {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button_click);
        mp.start();

        Intent ReturnHomeIntent = new Intent(this, HomePage.class);
        startActivity(ReturnHomeIntent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.pull_out_left);
    }
}
