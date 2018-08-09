package com.example.mike.mikemusic.data.source;

import com.example.mike.mikemusic.data.model.Track;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ThienNA on 01/08/2018.
 */

public interface TrackDatasource {

    interface RemoteDataSource {
        Observable<List<Track>> getTracksByGenre(String genrePath, int offset);
    }

    interface LocalDataSource {
    }
}
