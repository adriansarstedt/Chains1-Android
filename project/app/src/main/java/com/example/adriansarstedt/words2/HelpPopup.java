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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_popup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return HelpFragment1.newInstance("FirstFragment, Instance 1");
                case 1: return HelpFragment2.newInstance("SecondFragment, Instance 1");
                case 2: return HelpFragment2.newInstance("SecondFragment, Instance 2");
                default: return HelpFragment1.newInstance("FirstFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public void Close(View view) {
        finish();
        overridePendingTransition(0, 0);
    }
}
