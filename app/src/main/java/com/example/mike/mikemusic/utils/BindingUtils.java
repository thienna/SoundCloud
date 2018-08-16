package com.example.mike.mikemusic.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mike.mikemusic.R;

/**
 * Created by ThienNA on 03/08/2018.
 */

public class BindingUtils {
    @BindingAdapter("setAdapter")
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("onNavigationItemSelected")
    public static void setOnNavigationItemSelectedListener(
            BottomNavigationView view, BottomNavigationView.OnNavigationItemSelectedListener
            listener) {
        view.setOnNavigationItemSelectedListener(listener);
    }

    @BindingAdapter(value = {"app:imageUrl", "app:imageResource"}, requireAll = false)
    public static void loadImage(ImageView view, String imageUrl, int imageResource) {
        if (imageResource > 0) {
            Glide.with(view.getContext())
                    .load(imageResource)
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).centerCrop())
                    .into(view);
        } else {
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).centerCrop())
                    .into(view);
        }
    }

    @BindingAdapter({"endlessScrollListener"})
    public static void setScrollListener(RecyclerView recyclerView,
                                         RecyclerView.OnScrollListener onScrollListener) {
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @BindingAdapter({"formatDuration"})
    public static void formatDuration(TextView view,long number) {
        view.setText(Utils.parseMilliSecondsToTimer(number));
    }

    @BindingAdapter(value = {"app:playerImageUrl", "app:error"}, requireAll = false)
    public static void showImage(ImageView view, String imageUrl, Drawable error) {
        if (error == null) {
            Glide.with(view.getContext())
                    .asBitmap()
                    .load(imageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).centerCrop())
                    .into(view);
        } else {
            Glide.with(view.getContext())
                    .asBitmap()
                    .load(imageUrl)
                    .apply(new RequestOptions().placeholder(error).centerCrop())
                    .into(view);
        }
    }
}
