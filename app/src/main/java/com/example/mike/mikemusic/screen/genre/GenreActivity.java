package com.example.mike.mikemusic.screen.genre;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Genre;
import com.example.mike.mikemusic.databinding.ActivityGenreBinding;
import com.example.mike.mikemusic.screen.EndlessRecyclerViewOnScrollListener;
import com.example.mike.mikemusic.utils.Constants;

public class GenreActivity extends AppCompatActivity {

    private static final String EXTRA_GENRE = "EXTRA_GENRE";
    private GenreViewModel mViewModel;
    private Genre mGenre;
    private int mOffset;

    public static Intent getInstance(Context context, Genre genre) {
        Intent intent = new Intent(context, GenreActivity.class);
        intent.putExtra(EXTRA_GENRE, genre);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new GenreViewModel(this);
        mGenre = getIntent().getParcelableExtra(EXTRA_GENRE);
        mOffset = Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_OFFSET;

        ActivityGenreBinding activityGenreBinding = DataBindingUtil.setContentView(this, R.layout
                .activity_genre);
        activityGenreBinding.setViewModel(mViewModel);
        activityGenreBinding.setGenre(mGenre);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.loadTracksByGenre(mGenre.getPath(), mOffset);
        mViewModel.setEndlessRecyclerViewOnScrollListener(new EndlessRecyclerViewOnScrollListener() {
            @Override
            public void onLoadMore() {
                mOffset += 10;
                mViewModel.loadTracksByGenre(mGenre.getPath(), mOffset);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.onStop();
    }
}
