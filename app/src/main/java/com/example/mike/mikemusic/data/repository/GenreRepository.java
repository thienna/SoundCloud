package com.example.mike.mikemusic.data.repository;

import android.support.annotation.NonNull;

import com.example.mike.mikemusic.data.model.Genre;
import com.example.mike.mikemusic.data.source.GenreDataSource;
import com.example.mike.mikemusic.data.source.local.GenreLocalDataSource;

import java.util.List;

import io.reactivex.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ThienNA on 05/08/2018.
 */

public class GenreRepository implements GenreDataSource.LocalDataSource {

    private static GenreRepository sInstance;

    @NonNull
    private GenreDataSource.LocalDataSource mLocalDataSource;

    private GenreRepository(@NonNull GenreDataSource.LocalDataSource localDataSource) {
        mLocalDataSource = checkNotNull(localDataSource);
    }

    public static synchronized GenreRepository getInstance(
            GenreDataSource.LocalDataSource localDataSource) {
        if (sInstance == null) {
            sInstance = new GenreRepository(localDataSource);
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
        GenreLocalDataSource.destroyInstance();
    }

    @Override
    public Observable<List<Genre>> getListGenreLocal() {
        return mLocalDataSource.getListGenreLocal();
    }
}
