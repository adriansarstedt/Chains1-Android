package com.example.adriansarstedt.words2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment4 extends Fragment {

    TextView e1;
    GameDial gd;
    GameTextDisplay gt;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (gd != null) {
            if (isVisibleToUser) {
                gd.startTimer(new Runnable() {
                    @Override
                    public void run() {
                        gd.displayMessage("Game Over!", null, false);
                    }
                });

                gt.animateIn("A", "NT", 5000, gd, false);
                gt.animateOut("AN", "T", 6500, gd);
            } else {
                gd.reset();
                gt.setTextA("");
                gt.setTextB("A");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_4, container, false);

        e1 = (TextView) v.findViewById(R.id.e1f3);
        gd = (GameDial) v.findViewById(R.id.game_dial);
        gt = (GameTextDisplay) v.findViewById(R.id.game_text_display);

        Typeface custom_font_hairline = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Thin.ttf");

        e1.setTypeface(custom_font_hairline);

        e1.setText(getArguments().getString("t1"));
        gt.setTextA(getArguments().getString("t2"));
        gt.setTextB(getArguments().getString("t3"));

        return v;
    }

    public static HelpFragment4 newInstance(String t1, String t2, String t3) {

        HelpFragment4 f = new HelpFragment4();

        Bundle b = new Bundle();
        b.putString("t1", t1);
        b.putString("t2", t2);
        b.putString("t3", t3);

        f.setArguments(b);

        return f;
    }
}