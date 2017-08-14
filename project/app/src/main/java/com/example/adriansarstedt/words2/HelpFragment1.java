package com.example.adriansarstedt.words2;

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

        TextView tv = (TextView) v.findViewById(R.id.tvFirst);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static HelpFragment1 newInstance(String text) {

        HelpFragment1 f = new HelpFragment1();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}