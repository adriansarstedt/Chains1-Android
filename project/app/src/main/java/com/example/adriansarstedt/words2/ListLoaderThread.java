package com.example.adriansarstedt.words2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by adriansarstedt on 8/01/2017.
 */

public class ListLoaderThread extends AsyncTask<Integer, Void, Bitmap>{
    private final WeakReference<ImageView> imageViewReference;
    private Context context;
    Integer id;
    Boolean discovered;

    public ListLoaderThread(Context context, ImageView img, Boolean Discovered){
        imageViewReference = new WeakReference<ImageView>(img);
        this.context = context;
        this.discovered = Discovered;
    }

    @Override
    protected Bitmap doInBackground(Integer... Params) {
        id = Params[0];
        Bitmap colorBitmap = BitmapFactory.decodeResource(this.context.getResources(), id);
        if (colorBitmap == null || discovered) {
            return colorBitmap;
        } else {
            Bitmap greyScaleBitmap = toGrayscale(colorBitmap);
            return greyScaleBitmap;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (isCancelled()) {
            result = null;
        }

        if (imageViewReference != null && result != null) {
            final ImageView imageView = imageViewReference.get();
            final ListLoaderThread listLoaderThread =
                    getListLoaderThread(imageView);
            if (this == listLoaderThread && imageView != null) {
                imageView.setImageBitmap(result);
                final ScaleAnimation startGrowAnim = new ScaleAnimation(0f, 1f, 0f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                startGrowAnim.setDuration(1000);

                imageView.setAnimation(startGrowAnim);
                startGrowAnim.start();
            }
        }
    }

    private Bitmap toGrayscale(Bitmap bmpOriginal)
    {
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

    private static ListLoaderThread getListLoaderThread(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof GridDrawable) {
                final GridDrawable asyncDrawable = (GridDrawable) drawable;
                return asyncDrawable.getListLoaderThread();
            }
        }
        return null;
    }
}

