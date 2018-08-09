package com.example.mike.mikemusic.data.source;

import com.example.mike.mikemusic.data.model.Genre;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ThienNA on 05/08/2018.
 */

public interface GenreDataSource {
    interface LocalDataSource {
        Observable<List<Genre>> getListGenreLocal();
    }
}
