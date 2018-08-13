package com.example.mike.mikemusic.screen.personal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.databinding.ItemPersonalBinding;
import com.example.mike.mikemusic.screen.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThienNA on 12/08/2018.
 */

public class TrackOfflineRecyclerAdapter extends BaseRecyclerViewAdapter<Track,
        TrackOfflineRecyclerAdapter.ViewHolder> {

    private List<Track> mTracks;
    private PersonalViewModel mViewModel;
    private LayoutInflater mLayoutInflater;

    public TrackOfflineRecyclerAdapter(@NonNull Context context, PersonalViewModel viewModel,
                                       List<Track> tracks) {
        super(context);
        mViewModel = viewModel;
        mTracks = tracks;
    }

    @Override
    public void addItem(List<Track> data) {
        if (data == null) return;

        if (mTracks == null) {
            mTracks = new ArrayList<>();
        }

        mTracks.clear();
        mTracks.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemPersonalBinding binding = ItemPersonalBinding
                .inflate(mLayoutInflater, parent, false);
        binding.setViewModel(mViewModel);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = mTracks.get(position);
        if (track != null) {
            holder.bind(track);
        }
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPersonalBinding mBinding;
        private Track mTrack;

        public ViewHolder(ItemPersonalBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Track track) {
            if (track == null) return;
            mTrack = track;
            mBinding.setTrack(track);
            mBinding.executePendingBindings();
        }
    }
}
