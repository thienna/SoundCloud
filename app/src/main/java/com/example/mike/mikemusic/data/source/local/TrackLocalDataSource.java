package com.example.mike.mikemusic.data.source.local;

import com.example.mike.mikemusic.data.source.TrackDatasource;

/**
 * Created by ThienNA on 02/08/2018.
 */

public class TrackLocalDataSource implements TrackDatasource.LocalDataSource {
    private static TrackLocalDataSource sInstance;

    private TrackLocalDataSource() {
    }

    public static synchronized TrackLocalDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new TrackLocalDataSource();
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }
}
