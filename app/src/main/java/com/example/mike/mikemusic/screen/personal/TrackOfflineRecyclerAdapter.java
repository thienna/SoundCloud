package com.example.mike.mikemusic.screen.personal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.databinding.ItemPersonalBinding;
import com.example.mike.mikemusic.screen.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by ThienNA on 12/08/2018.
 */

public class TrackOfflineRecyclerAdapter extends BaseRecyclerViewAdapter<Track,
        TrackOfflineRecyclerAdapter.ViewHolder> {

    public TrackOfflineRecyclerAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public void addItem(List<Track> data) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPersonalBinding mBinding;

        public ViewHolder(ItemPersonalBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
