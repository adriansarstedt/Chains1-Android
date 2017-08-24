package com.example.adriansarstedt.words2;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class HelpPopup extends FragmentActivity {

    MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_popup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }

        adapter = new MyPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
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
                case 2: return HelpFragment3.newInstance("WATCH THE DIAL TO MAKE SURE YOUR TIME DOESN'T RUN OUT", "A", "");
                case 3: return HelpFragment4.newInstance("WATCH THE DIAL TO MAKE SURE YOUR TIME DOESN'T RUN OUT", "A", "");
                default: return HelpFragment1.newInstance("a", "b", "c", "d");
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void Close(View view) {
        finish();
        overridePendingTransition(0, 0);
    }
}
