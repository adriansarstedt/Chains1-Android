package com.example.adriansarstedt.words2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HelpFragment3 extends Fragment {

    TextView e1;
    ArcView av;
    GameTextDisplay gt;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (gt != null) {
            if (isVisibleToUser) {
                ArcShrinkAnimation asa = new ArcShrinkAnimation(av);
                asa.setDuration(10000);
                av.startAnimation(asa);

                gt.animateIn("", "A", 200, null, false);
                gt.animateIn("A", "NT", 5000, av, false);
                gt.animateOut("AN", "T", 6000, av);
                gt.animateOut("T", "", 10000, null);
                gt.animateIn("", "OTHEWISE YOU LOSE", 11000, null, true);
            } else {
                gt.resetView();
                gt.setTextB("A");
                av.clearAnimation();

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_3, container, false);

        e1 = (TextView) v.findViewById(R.id.e1f3);
        av = (ArcView) v.findViewById(R.id.arc_view);
        gt = (GameTextDisplay) v.findViewById(R.id.game_text_display);

        Typeface custom_font_hairline = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Thin.ttf");

        e1.setTypeface(custom_font_hairline);

        e1.setText(getArguments().getString("t1"));
        gt.setTextA(getArguments().getString("t2"));
        gt.setTextB(getArguments().getString("t3"));

        return v;
    }

    public static HelpFragment3 newInstance(String t1, String t2, String t3) {

        HelpFragment3 f = new HelpFragment3();

        Bundle b = new Bundle();
        b.putString("t1", t1);
        b.putString("t2", t2);
        b.putString("t3", t3);

        f.setArguments(b);

        return f;
    }
}