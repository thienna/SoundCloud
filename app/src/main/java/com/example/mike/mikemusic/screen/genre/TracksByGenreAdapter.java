package com.example.mike.mikemusic.screen.genre;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.databinding.ItemGenreDetailBinding;
import com.example.mike.mikemusic.screen.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThienNA on 05/08/2018.
 */

public class TracksByGenreAdapter
        extends BaseRecyclerViewAdapter<Track, TracksByGenreAdapter.GenreDetailViewHolder> {

    private GenreViewModel mViewModel;
    private LayoutInflater mLayoutInflater;
    private List<Track> mTracks;

    TracksByGenreAdapter(Context context, GenreViewModel viewModel, List<Track> tracks) {
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

        mTracks.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public GenreDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemGenreDetailBinding binding = ItemGenreDetailBinding
                .inflate(mLayoutInflater, parent, false);
        binding.setViewModel(mViewModel);
        return new GenreDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GenreDetailViewHolder holder, int position) {
        Track track = mTracks.get(position);
        if (track != null) {
            holder.bind(track);
        }
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    static class GenreDetailViewHolder extends RecyclerView.ViewHolder {

        private ItemGenreDetailBinding mBinding;

        public GenreDetailViewHolder(ItemGenreDetailBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Track track) {
            if (track == null) return;
            itemView.getResources().getDimension(R.dimen.dp_10);
            mBinding.setPosition(getLayoutPosition());
            mBinding.setTrack(track);
            mBinding.executePendingBindings();
        }
    }
}
