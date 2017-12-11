package com.example.adriansarstedt.words2;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import java.util.ArrayList;
import java.util.Arrays;

public class ResearchCenterHelp extends Activity {

    ColorDrawable Background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }

        Background = new ColorDrawable(Color.parseColor("#ccFF0033"));
        findViewById(R.id.activity_entry_info).setBackground(Background);

        Typeface custom_font_hairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        String DiscoveredCompressed = PreferenceManager.getDefaultSharedPreferences(this).getString("DiscoveredAnimals", "");
        ArrayList<String> DiscoveredList = new ArrayList<String>(Arrays.asList(DiscoveredCompressed.split("-")));
    }

    public void runEnterAnimation() {

    }

    public void runExitAnimation(final Runnable endAction) {
        endAction.run();
    }

    @Override
    public void onBackPressed() {
        runExitAnimation(new Runnable() {
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    public void returnToResearchCenter(View view) {
        onBackPressed();
    }
}
