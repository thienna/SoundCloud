package com.example.mike.mikemusic.screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by ThienNA on 05/08/2018.
 */

public abstract class BaseRecyclerViewAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView
        .Adapter<V> {

    protected final Context mContext;

    protected BaseRecyclerViewAdapter(@NonNull Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    public abstract void addItem(List<T> data);

    public interface OnRecyclerViewItemClickListener<T> {
        void onItemRecyclerViewClick(T item, RecyclerView recyclerView);
    }
}
