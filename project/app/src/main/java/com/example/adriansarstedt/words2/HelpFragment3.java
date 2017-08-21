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
    Button tb;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ArcShrinkAnimation asa = new ArcShrinkAnimation(av, 360);
            asa.setDuration(5000);
            av.startAnimation(asa);

            gt.animateIn("A", "NT", 3000, av);
            gt.animateOut("AN", "T", 4000, av);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_3, container, false);

        e1 = (TextView) v.findViewById(R.id.e1f3);
        av = (ArcView) v.findViewById(R.id.arc_view);
        gt = (GameTextDisplay) v.findViewById(R.id.game_text_display);
        tb = (Button) v.findViewById(R.id.testButton);

        Typeface custom_font_hairline = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Thin.ttf");

        e1.setTypeface(custom_font_hairline);

        e1.setText(getArguments().getString("t1"));
        gt.setTextA(getArguments().getString("t2"));
        gt.setTextB(getArguments().getString("t3"));


        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonPress();
            }
        });

        return v;
    }

    public static HelpFragment3 newInstance(String t1, String t2, String t3, String t4) {

        HelpFragment3 f = new HelpFragment3();

        Bundle b = new Bundle();
        b.putString("t1", t1);
        b.putString("t2", t2);
        b.putString("t3", t3);
        b.putString("t4", t4);

        f.setArguments(b);

        return f;
    }

    public void ButtonPress() {
        gt.animateOut("A", "T", 2000, av);
    }
}