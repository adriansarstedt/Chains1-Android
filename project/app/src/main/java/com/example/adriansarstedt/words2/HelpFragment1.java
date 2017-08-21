package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class HelpFragment1 extends Fragment {

    TextView e1, e2, e3, e4;
    View c;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (e3 != null) {
            if (isVisibleToUser) {
                e3.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        e3.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int translationA = (c.getWidth() - e3.getWidth()) / 2;
                        int translationB = (c.getWidth() - e2.getWidth()) / 2;

                        ObjectAnimator inAnimation = ObjectAnimator.ofFloat(e3, "X", 1000, translationA + e2.getWidth() / 2);
                        inAnimation.setDuration(800);
                        inAnimation.start();

                        ObjectAnimator outAnimation = ObjectAnimator.ofFloat(e2, "X", translationB, translationB - e3.getWidth() / 2);
                        outAnimation.setDuration(600);
                        outAnimation.start();
                    }
                });
            } else {
                e3.setTranslationX(3000);
                e3.setText("E");
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_1, container, false);

        e1 = (TextView) v.findViewById(R.id.e1f1);
        e2 = (TextView) v.findViewById(R.id.e2f1);
        e3 = (TextView) v.findViewById(R.id.e3f1);
        e4 = (TextView) v.findViewById(R.id.e4f1);
        c = v.findViewById(R.id.textholder);

        Typeface custom_font_hairline = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Thin.ttf");

        e1.setTypeface(custom_font_hairline);
        e2.setTypeface(custom_font_hairline);
        e3.setTypeface(custom_font_hairline);
        e4.setTypeface(custom_font_hairline);

        e1.setText(getArguments().getString("t1"));
        e2.setText(getArguments().getString("t2"));
        e3.setText(getArguments().getString("t3"));
        e4.setText(getArguments().getString("t4"));

        e3.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                e3.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int translationA = (c.getWidth()-e3.getWidth())/2;
                int translationB = (c.getWidth()-e2.getWidth())/2;

                ObjectAnimator inAnimation = ObjectAnimator.ofFloat(e3, "X", 1000, translationA+e2.getWidth()/2);
                inAnimation.setDuration(800);
                inAnimation.start();

                ObjectAnimator outAnimation = ObjectAnimator.ofFloat(e2, "X", translationB, translationB-e3.getWidth()/2);
                outAnimation.setDuration(600);
                outAnimation.start();
            }
        });

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