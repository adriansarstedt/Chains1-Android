package com.example.adriansarstedt.words2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirstHelpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_help_frag, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvFirst);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static FirstHelpFragment newInstance(String text) {

        FirstHelpFragment f = new FirstHelpFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}