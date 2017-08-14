package com.example.adriansarstedt.words2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_2, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvSecond);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static HelpFragment2 newInstance(String text) {

        HelpFragment2 f = new HelpFragment2();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
