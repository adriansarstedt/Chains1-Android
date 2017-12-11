package com.example.adriansarstedt.words2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class HelpFragment4 extends Fragment {

    TextView e1, e2, e3;
    View ProgressBar, ProgressBarHolder;

    float DiscoveredCount, PercentageDiscovered, TotalCount;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            e2.setText(String.valueOf(Math.round(DiscoveredCount)) +" OF "+String.valueOf(Math.round(TotalCount)));

            ResizeWidthAnimation ProgressBarAnimation = new ResizeWidthAnimation(ProgressBar,
                    Math.round(ProgressBarHolder.getWidth()*PercentageDiscovered));
            ProgressBarAnimation.setDuration(1000);
            ProgressBar.startAnimation(ProgressBarAnimation);
            System.out.println("testing");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_4, container, false);

        e1 = (TextView) v.findViewById(R.id.e1f4);
        e2 = (TextView) v.findViewById(R.id.e2f4);
        e3 = (TextView) v.findViewById(R.id.e3f4);

        Typeface custom_font_hairline = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Thin.ttf");

        e1.setTypeface(custom_font_hairline);
        e2.setTypeface(custom_font_hairline);
        e3.setTypeface(custom_font_hairline);

        ProgressBar = (View) v.findViewById(R.id.progressBar);
        ProgressBarHolder = (View) v.findViewById(R.id.progressBarHolder);

        String DiscoveredCompressed = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("DiscoveredAnimals", "");
        ArrayList<String> DiscoveredList = new ArrayList<String>(Arrays.asList(DiscoveredCompressed.split("-")));
        DiscoveredCount = DiscoveredList.size();
        TotalCount = Globals.Animals.size();
        PercentageDiscovered = DiscoveredCount/TotalCount;

        e2.setText(String.valueOf(Math.round(DiscoveredCount)) +" OF "+String.valueOf(Math.round(TotalCount)));

        return v;
    }

    public static HelpFragment4 newInstance() {
        return new HelpFragment4();
    }
}