package com.example.adriansarstedt.words2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment2 extends Fragment {

    TextView e1, e2;
    GameTextDisplay gt;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (gt != null) {
            if (isVisibleToUser) {
                gt.animateIn(getArguments().getString("t2"), getArguments().getString("t3"), 400, null, false);
                gt.animateOut("ECHIDN", "A", 2500, null);
            } else {
                gt.resetView();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_2, container, false);

        e1 = (TextView) v.findViewById(R.id.e1f2);
        e2 = (TextView) v.findViewById(R.id.e2f2);
        gt = (GameTextDisplay) v.findViewById(R.id.game_text_display);

        Typeface custom_font_hairline = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Thin.ttf");

        e1.setTypeface(custom_font_hairline);
        e2.setTypeface(custom_font_hairline);

        e1.setText(getArguments().getString("t1"));
        e2.setText(getArguments().getString("t4"));

        return v;
    }

    public static HelpFragment2 newInstance(String t1, String t2, String t3, String t4) {

        HelpFragment2 f = new HelpFragment2();

        Bundle b = new Bundle();
        b.putString("t1", t1);
        b.putString("t2", t2);
        b.putString("t3", t3);
        b.putString("t4", t4);

        f.setArguments(b);

        return f;
    }
}