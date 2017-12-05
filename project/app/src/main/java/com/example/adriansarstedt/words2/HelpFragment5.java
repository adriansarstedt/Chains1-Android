package com.example.adriansarstedt.words2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpFragment5 extends Fragment {

    TextView e1, e2, e3, e4;
    ImageView iv1, iv2;

    Handler animationHandler;
    Runnable animationRunnable;

    ShakeAnimation ShakeAnimator;

    int count = 0;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (animationHandler != null) {
            if (isVisibleToUser) {
                animationHandler.postDelayed(animationRunnable, 1000);
            } else {
                animationHandler.removeCallbacks(animationRunnable);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.help_frag_5, container, false);

        e1 = (TextView) v.findViewById(R.id.e1f5);
        e2 = (TextView) v.findViewById(R.id.e2f5);
        e3 = (TextView) v.findViewById(R.id.e3f5);
        e4 = (TextView) v.findViewById(R.id.e4f5);

        iv1 = (ImageView) v.findViewById(R.id.iv1f5);
        iv2 = (ImageView) v.findViewById(R.id.iv2f5);

        Typeface custom_font_hairline = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Thin.ttf");

        e1.setTypeface(custom_font_hairline);
        e2.setTypeface(custom_font_hairline);
        e3.setTypeface(custom_font_hairline);
        e4.setTypeface(custom_font_hairline);

        setupAnimations();

        return v;
    }

    public static HelpFragment5 newInstance() {
        return new HelpFragment5();
    }

    public void setupAnimations() {

        animationHandler = new Handler();
        ShakeAnimator = new ShakeAnimation(getContext());

        animationRunnable = new Runnable() {
            @Override
            public void run() {
                count++;
                if (count%2==1) {
                    ShakeAnimator.runAniamtionLarge(iv1);
                } else {
                    ShakeAnimator.runAniamtionLarge(iv2);
                }
                animationHandler.postDelayed(this, 2000);
            }
        };
    }
}