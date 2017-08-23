package com.example.adriansarstedt.words2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment1 extends Fragment {

    TextView e1, e2;
    GameTextDisplay gt;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (gt != null) {
            if (isVisibleToUser) {
                gt.animateIn("", "E", 300, null, false);
            } else {
                gt.resetView();
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_1, container, false);

        e1 = (TextView) v.findViewById(R.id.e1f1);
        gt = (GameTextDisplay) v.findViewById(R.id.game_text_display);
        e2 = (TextView) v.findViewById(R.id.e2f1);

        Typeface custom_font_hairline = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Thin.ttf");

        e1.setTypeface(custom_font_hairline);
        e2.setTypeface(custom_font_hairline);

        e1.setText(getArguments().getString("t1"));
        e2.setText(getArguments().getString("t4"));
        gt.setTextA(getArguments().getString("t2"));
        gt.setTextB(getArguments().getString("t3"));

        gt.resetView();
        gt.animateIn("", "E", 300, null, false);

        return v;
    }

    public static HelpFragment1 newInstance(String t1, String t2, String t3, String t4) {

        HelpFragment1 f = new HelpFragment1();

        Bundle b = new Bundle();
        b.putString("t1", t1);
        b.putString("t2", t2);
        b.putString("t3", t3);
        b.putString("t4", t4);

        f.setArguments(b);

        return f;
    }
}