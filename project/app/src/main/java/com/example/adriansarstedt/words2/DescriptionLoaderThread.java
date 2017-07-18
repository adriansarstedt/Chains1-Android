package com.example.adriansarstedt.words2;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by adriansarstedt on 16/01/2017.
 */

public class DescriptionLoaderThread extends AsyncTask<Integer, Void, CharSequence> {
    private final WeakReference<TextView> TextViewReference;
    private Context context;
    Boolean discovered;

    public DescriptionLoaderThread(Context context, TextView txt, Boolean Discovered) {
        TextViewReference = new WeakReference<TextView>(txt);
        this.context = context;
        this.discovered = Discovered;
    }

    @Override
    protected CharSequence doInBackground(Integer... Params) {
        int position = Params[0];
        AssetManager assetManager = context.getAssets();
        InputStream input;

        CharSequence finalText = "";

        try {
            input = assetManager.open(Globals.Animals.get(position).toLowerCase() + "information.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            String text = new String(buffer);
            if (!discovered) {
                text = text.replaceAll(Globals.Animals.get(position), generateSecretString(Globals.Animals.get(position)));
                text = text.replaceAll(Globals.Animals.get(position).toLowerCase(), generateSecretString(Globals.Animals.get(position).toLowerCase()));
                text = text.replaceAll("\'", "");
            }

            String[] groups = text.split("KKheading");

            if (groups.length == 1) {
                finalText = text;
            } else {
                for (int p = 0; p < groups.length / 2; p++) {

                    SpannableString span1 = new SpannableString(groups[2 * p + 1]);
                    span1.setSpan(new AbsoluteSizeSpan(60), 0, groups[2 * p + 1].length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    span1.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, groups[2 * p + 1].length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                    SpannableString span2 = new SpannableString(groups[2 * p + 2]);
                    span2.setSpan(new AbsoluteSizeSpan(40), 0, groups[2 * p + 2].length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    span2.setSpan(new ForegroundColorSpan(Color.parseColor("#cc381900")), 0, groups[2 * p + 2].length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                    finalText = TextUtils.concat(finalText, TextUtils.concat(span1, "", span2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (finalText);
    }

    public String generateSecretString(String key) {
        String RepeatSequence = new String(new char[key.length()-1]).replace("\0", "_");
        return key.substring(0, 1) + RepeatSequence;
    }

    @Override
    protected void onPostExecute(CharSequence result) {

        if (TextViewReference != null && result != null) {
            final TextView imageView = TextViewReference.get();
            imageView.setText(result);
        }
    }
}