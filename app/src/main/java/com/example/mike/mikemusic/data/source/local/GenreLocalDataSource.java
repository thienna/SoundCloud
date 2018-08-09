package com.example.mike.mikemusic.data.source.local;

import com.example.mike.mikemusic.data.model.Genre;
import com.example.mike.mikemusic.data.model.GenreType;
import com.example.mike.mikemusic.data.source.GenreDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ThienNA on 05/08/2018.
 */

public class GenreLocalDataSource implements GenreDataSource.LocalDataSource {
    private static GenreLocalDataSource sInstance;

    private GenreLocalDataSource() {
    }

    public static synchronized GenreLocalDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new GenreLocalDataSource();
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    @Override
    public Observable<List<Genre>> getListGenreLocal() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(GenreType.ALL_MUSIC));
        genres.add(new Genre(GenreType.ALL_AUDIO));
        genres.add(new Genre(GenreType.ALTERNATIVE_ROCK));
        genres.add(new Genre(GenreType.CLASSICAL));
        genres.add(new Genre(GenreType.AMBIENT));
        genres.add(new Genre(GenreType.COUNTRY));

        return Observable.just(genres);
    }
}
