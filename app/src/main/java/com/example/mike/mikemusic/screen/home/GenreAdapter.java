package com.example.mike.mikemusic.screen.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Genre;
import com.example.mike.mikemusic.data.model.GenreType;
import com.example.mike.mikemusic.databinding.ItemGenreBinding;
import com.example.mike.mikemusic.screen.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThienNA on 03/08/2018.
 */

public class GenreAdapter extends BaseRecyclerViewAdapter<Genre, GenreAdapter.GenreViewHolder> {

    private List<Genre> mGenres;
    private HomeViewModel mViewModel;
    private LayoutInflater mLayoutInflater;

    public GenreAdapter(Context context, HomeViewModel viewModel, List<Genre> genres) {
        super(context);
        mViewModel = viewModel;
        mGenres = genres;
    }

    @Override
    public void addItem(List<Genre> data) {
        if (data == null) return;

        if (mGenres == null) {
            mGenres = new ArrayList<>();
        }

        mGenres.clear();
        mGenres.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemGenreBinding binding = ItemGenreBinding
                .inflate(mLayoutInflater, parent, false);
        binding.setViewModel(mViewModel);
        return new GenreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        Genre genre = mGenres.get(position);
        if (genre != null) {
            holder.bind(genre);
        }
    }

    @Override
    public int getItemCount() {
        return mGenres != null ? mGenres.size() : 0;
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {

        private ItemGenreBinding mBinding;
        private Genre mGenre;

        public GenreViewHolder(ItemGenreBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Genre genre) {
            if (genre == null) return;
            mGenre = genre;
            switch (mGenre.getPath()) {
                case GenreType.ALL_MUSIC:
                    setGenreProperties(R.string.all_music, R.drawable.genre_music, R.drawable.alternative_rock_small);
                    break;
                case GenreType.ALL_AUDIO:
                    setGenreProperties(R.string.all_audio, R.drawable.genre_all_audio, R.drawable
                            .all_audio_small);
                    break;
                case GenreType.ALTERNATIVE_ROCK:
                    setGenreProperties(R.string.alternative_rock, R.drawable.genre_alternative_rock, R.drawable
                            .ambient_small);
                    break;
                case GenreType.CLASSICAL:
                    setGenreProperties(R.string.classical, R.drawable.genre_classical, R.drawable
                            .classical_small);
                    break;
                case GenreType.AMBIENT:
                    setGenreProperties(R.string.ambient, R.drawable.genre_ambient, R.drawable
                            .all_music_small);
                    break;
                case GenreType.COUNTRY:
                    setGenreProperties(R.string.country, R.drawable.genre_country, R.drawable
                            .country_small);
                    break;
            }
            mBinding.setGenre(genre);
            mBinding.executePendingBindings();
        }

        private void setGenreProperties(int titleResourceId, int imageResourceId, int
                smallImageResourceId){
            mGenre.setResourceImage(imageResourceId);
            mGenre.setTitle(itemView.getResources().getString(titleResourceId));
            mGenre.setResourceImageSmall(smallImageResourceId);
        }
    }
}
