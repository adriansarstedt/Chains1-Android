package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class AlertPopup extends Activity {

    ColorDrawable Background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_popup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#FF0033"));
        }

        Background = new ColorDrawable(Color.parseColor("#55FF0033"));
        findViewById(R.id.activity_alert_popup).setBackground(Background);

        String Title = getIntent().getStringExtra("Title");
        String Description = getIntent().getStringExtra("Description");
        String Type = getIntent().getStringExtra("Type");

        TextView TitleView = (TextView) findViewById(R.id.AlertTitle);
        TextView DescriptionView = (TextView) findViewById(R.id.AlertDescription);

        Button LeftButton = (Button) findViewById(R.id.AlertCancel);
        Button RightButton = (Button) findViewById(R.id.AlertContinue);

        if (Type != null && Type.equals("adriansarstedt")) {
            LeftButton.setText("See More");
            LeftButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://adriansarstedt.com/")));
                }
            });
            RightButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    runExitAnimation();
                }
            });
        }

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lato-Thin.ttf");
        TitleView.setTypeface(custom_font);
        DescriptionView.setTypeface(custom_font);
        LeftButton.setTypeface(custom_font);
        RightButton.setTypeface(custom_font);

        TitleView.setText(Title);
        DescriptionView.setText(Description);

        if (savedInstanceState == null) {
            runEnterAnimation();
        }
    }

    public void runEnterAnimation() {
        View Popup = findViewById(R.id.PopupView);

        Animation SlideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pull_in_left);
        Popup.startAnimation(SlideIn);

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(Background, "alpha", 0, 255);
        bgAnim.setDuration(1000);
        bgAnim.start();
    }

    public void runExitAnimation() {
        View Popup = findViewById(R.id.PopupView);

        Popup.animate().setDuration(500).translationX(1500).withEndAction(new Runnable() {
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(Background, "alpha", 255, 0);
        bgAnim.setDuration(300);
        bgAnim.start();
    }

    public void Cancel(View view) {
        runExitAnimation();
    }

    public void Continue(View view) {
        runExitAnimation();

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("DiscoveredAnimals", "researchcenter").apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("EggCount", 0).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("HomeOpened", 0).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("ResearchCenterOpened", 0).apply();
        SharedPreferences HighScoreManager = this.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        SharedPreferences.Editor HighScoreEditor = HighScoreManager.edit();
        HighScoreEditor.putInt(Globals.User+Globals.Difficulty, 0);
        HighScoreEditor.apply();
    }
}
