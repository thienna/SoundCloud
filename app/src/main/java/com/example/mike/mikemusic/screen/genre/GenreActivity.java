package com.example.mike.mikemusic.screen.genre;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Genre;
import com.example.mike.mikemusic.databinding.ActivityGenreBinding;

public class GenreActivity extends AppCompatActivity {

    private static final String EXTRA_GENRE = "EXTRA_GENRE";
    private GenreViewModel mViewModel;
    private Genre mGenre;

    public static Intent getInstance(Context context, Genre genre) {
        Intent intent = new Intent(context, GenreActivity.class);
        intent.putExtra(EXTRA_GENRE, genre);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGenre = getIntent().getParcelableExtra(EXTRA_GENRE);
        mViewModel = new GenreViewModel(this, mGenre);

        ActivityGenreBinding activityGenreBinding = DataBindingUtil.setContentView(this, R.layout
                .activity_genre);
        activityGenreBinding.setViewModel(mViewModel);
        activityGenreBinding.setGenre(mGenre);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.onStop();
    }
}
