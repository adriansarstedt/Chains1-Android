package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class EntryInfo extends Activity {

    int position, imageY, imageX, imageWidth, imageHeight, ImageTranslationX, ImageTranslationY;
    float ScaleX, ScaleY;

    View ImageFrame, MainScroll;
    ImageView EntryImage;
    TextView EntryName;
    boolean Discovered;
    Context cx;
    ColorDrawable Background;
    ImageButton BackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#FF0033"));
        }

        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("Info" + ".position");
        imageY = bundle.getInt("Info" + ".top");
        imageX = bundle.getInt("Info" + ".left");
        imageWidth = bundle.getInt("Info" + ".width");
        imageHeight = bundle.getInt("Info" + ".height");

        Background = new ColorDrawable(Color.parseColor("#ccFF0033"));
        findViewById(R.id.activity_entry_info).setBackground(Background);

        Typeface custom_font_hairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");
        EntryName = (TextView) findViewById(R.id.EntryName);
        EntryName.setTypeface(custom_font_hairline);

        String DiscoveredCompressed = PreferenceManager.getDefaultSharedPreferences(this).getString("DiscoveredAnimals", "");
        ArrayList<String> DiscoveredList = new ArrayList<String>(Arrays.asList(DiscoveredCompressed.split("-")));
        Discovered = DiscoveredList.contains(Globals.Animals.get(position));

        EntryImage = (ImageView) findViewById(R.id.Image);
        ImageFrame = (View) findViewById(R.id.ImageFrame);
        cx = this;

        LoadSmallImage();

        TextView EntryInfo = (TextView) findViewById(R.id.InfoContent);
        EntryInfo.setTypeface(custom_font_hairline);
        final DescriptionLoaderThread task = new DescriptionLoaderThread(cx, EntryInfo, Discovered);
        task.execute(position);

        if (savedInstanceState == null) {
            ViewTreeObserver observer = ImageFrame.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    ImageFrame.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    ImageFrame.getLocationOnScreen(screenLocation);
                    ImageTranslationX = imageX - screenLocation[0];
                    ImageTranslationY = imageY - screenLocation[1];

                    ScaleX = (float) imageWidth / ImageFrame.getWidth();
                    ScaleY = (float) imageHeight / ImageFrame.getHeight();

                    runEnterAnimation();

                    return true;
                }
            });
        }
    }

    public void LoadSmallImage() {
        Bitmap ImageBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(Globals.Animals.get(position).toLowerCase() + "imagesmall",
                        "drawable", getPackageName()));

        if (Discovered) {
            ImageFrame.setBackgroundResource(R.drawable.image_background_discovered);
            EntryName.setText("The " + Globals.Animals.get(position));
        } else {
            ImageFrame.setBackgroundResource(R.drawable.image_backround);
            ImageBitmap = toGrayscale(ImageBitmap);
            EntryName.setText("The " + generateSecretString(Globals.Animals.get(position)));
        }

        EntryImage.setImageBitmap(ImageBitmap);
    }

    public void runEnterAnimation() {

        ImageFrame.setPivotX(0);
        ImageFrame.setPivotY(0);
        ImageFrame.setScaleX(ScaleX);
        ImageFrame.setScaleY(ScaleY);
        ImageFrame.setTranslationX(ImageTranslationX);
        ImageFrame.setTranslationY(ImageTranslationY);
        ImageFrame.animate().setDuration(500).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0);

        BackButton = (ImageButton) findViewById(R.id.BackButton);
        BackButton.setScaleX(0); BackButton.setScaleY(0);
        BackButton.setPivotY(Math.round(BackButton.getWidth()/2));
        BackButton.setPivotX(Math.round(BackButton.getWidth()/2));
        BackButton.animate().setDuration(500).
                scaleY(1).scaleX(1);

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(Background, "alpha", 0, 255);
        bgAnim.setDuration(1000);
        bgAnim.start();

        MainScroll = (View) findViewById(R.id.MainScroll);
        MainScroll.setTranslationY(1000);
        MainScroll.animate().setDuration(1000).translationY(0).
                withEndAction(new Runnable() {
                    public void run() {
                        final LargeImageLoaderThread task = new LargeImageLoaderThread(cx, EntryImage, Discovered);
                        task.execute(getResources().getIdentifier(Globals.Animals.get(position).toLowerCase() + "image",
                                "drawable", getPackageName()));
                    }
                });
    }

    public void runExitAnimation(final Runnable endAction) {

        BackButton.animate().setDuration(500).
                scaleY(0).scaleX(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                ImageFrame.animate().setDuration(1000).
                        scaleX(ScaleX).scaleY(ScaleY).
                        translationX(ImageTranslationX).translationY(ImageTranslationY).
                        withEndAction(endAction);
                MainScroll.animate().setDuration(1000).translationY(1000);
            }
        });

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(Background, "alpha", 255, 0);
        bgAnim.setDuration(1000);
        bgAnim.start();

        LoadSmallImage();
    }

    @Override
    public void onBackPressed() {
        runExitAnimation(new Runnable() {
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    private Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public String generateSecretString(String key) {
        String RepeatSequence = new String(new char[key.length()-1]).replace("\0", "_");
        return key.substring(0, 1) + RepeatSequence;
    }

    public void returnToResearchCenter(View view) {
        onBackPressed();
    }
}
