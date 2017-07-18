package com.example.adriansarstedt.words2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by adriansarstedt on 9/01/2017.
 */

public class GridDrawable extends BitmapDrawable {
    private final WeakReference<ListLoaderThread> ListLoaderThreadReference;

    public GridDrawable(Resources res, Bitmap bitmap,
                         ListLoaderThread listLoaderThread) {
        super(res, bitmap);
        ListLoaderThreadReference =
                new WeakReference<ListLoaderThread>(listLoaderThread);
    }

    public ListLoaderThread getListLoaderThread() {
        return ListLoaderThreadReference.get();
    }
}
