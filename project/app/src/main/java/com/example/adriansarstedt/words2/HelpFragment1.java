package com.example.adriansarstedt.words2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_1, container, false);

        TextView e1 = (TextView) v.findViewById(R.id.e1f1);
        TextView e2 = (TextView) v.findViewById(R.id.e2f1);
        TextView e3 = (TextView) v.findViewById(R.id.e3f1);
        TextView e4 = (TextView) v.findViewById(R.id.e4f1);

        Typeface custom_font_hairline = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Thin.ttf");

        e1.setTypeface(custom_font_hairline);
        e2.setTypeface(custom_font_hairline);
        e3.setTypeface(custom_font_hairline);
        e4.setTypeface(custom_font_hairline);

        e1.setText(getArguments().getString("t1"));
        e2.setText(getArguments().getString("t2"));
        e3.setText(getArguments().getString("t3"));
        e4.setText(getArguments().getString("t4"));

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