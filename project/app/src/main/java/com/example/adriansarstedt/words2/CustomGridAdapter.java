package com.example.adriansarstedt.words2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by adriansarstedt on 5/01/2017.
 */

public class CustomGridAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> listItems, DiscoveredList;

    static class ViewHolder {
        ImageView Picture;
        View Tile;
    }

    public CustomGridAdapter(Activity context, ArrayList<String> InputList) {

        super(context, R.layout.custom_tile, InputList);
        this.context = context;
        this.listItems = InputList;
        String DiscoveredCompressed = PreferenceManager.getDefaultSharedPreferences(this.context).getString("DiscoveredAnimals", "");
        this.DiscoveredList = new ArrayList<String>(Arrays.asList(DiscoveredCompressed.split("-")));

    }

    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;

        if(view==null){

            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.custom_tile, parent, false);

            holder = new ViewHolder();
            holder.Picture = (ImageView) view.findViewById(R.id.Picture);
            holder.Tile = (View) view.findViewById(R.id.Tile);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.Picture.setImageBitmap(null);

        Bitmap PlaceHolderBitmap = null;
        Integer id = context.getResources().getIdentifier(listItems.get(position).toLowerCase() + "imagesmall", "drawable", context.getPackageName());
        Boolean Discovered = DiscoveredList.contains(listItems.get(position));

        if (Discovered) {
            holder.Tile.setBackgroundResource(R.drawable.image_background_discovered);
        } else {
            holder.Tile.setBackgroundResource(R.drawable.image_background_undiscovered);
        }

        if (cancelPotentialWork(id, holder.Picture)) {

            final ScaleAnimation startGrowAnim = new ScaleAnimation(0f, 1f, 0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            startGrowAnim.setDuration(1000);

            holder.Tile.setAnimation(startGrowAnim);
            startGrowAnim.start();

            final ListLoaderThread task = new ListLoaderThread(context, holder.Picture, Discovered);
            final GridDrawable gridDrawable =
                    new GridDrawable(context.getResources(), PlaceHolderBitmap, task);
            holder.Picture.setImageDrawable(gridDrawable);
            task.execute(id);
        }

        return view;
    };

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final ListLoaderThread listLoaderThread = getListLoaderThread(imageView);

        if (listLoaderThread != null) {
            final int bitmapData = listLoaderThread.id;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task
                listLoaderThread.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
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
