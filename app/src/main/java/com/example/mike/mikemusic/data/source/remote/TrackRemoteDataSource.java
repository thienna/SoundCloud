package com.example.mike.mikemusic.data.source.remote;

import com.example.mike.mikemusic.data.model.Collection;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.data.source.TrackDatasource;
import com.example.mike.mikemusic.data.source.api.service.ApiSoundCloud;
import com.example.mike.mikemusic.data.source.api.service.SoundCloudServiceClient;
import com.example.mike.mikemusic.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ThienNA on 02/08/2018.
 */

public class TrackRemoteDataSource implements TrackDatasource.RemoteDataSource {

    private static TrackRemoteDataSource sInstance;
    private ApiSoundCloud mApiSoundCloud;

    private TrackRemoteDataSource() {
        mApiSoundCloud = SoundCloudServiceClient.getInstance();
    }

    public static synchronized TrackRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new TrackRemoteDataSource();
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    @Override
    public Observable<List<Track>> getTracksByGenre(String genrePath, int offset) {

        Observable<List<Track>> collectionResultObservable = mApiSoundCloud
                .getTracksByGenre(
                        Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_GENRE + genrePath,
                        offset,
                        Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_KIND,
                        Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_LIMIT,
                        Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_CLIENT_ID)
                .map(collectionResult -> {
                    List<Track> results = new ArrayList<>();
                    for (Collection collection : collectionResult.getCollection()) {
                        results.add(collection.getTrack());
                    }
                    return results;
                });

        return collectionResultObservable;
    }

    @Override
    public Observable<List<Track>> searchTrack(String query, int offset) {

        Observable<List<Track>> collectionResultObservable = mApiSoundCloud
                .searchTracks(Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_LIMIT, offset,
                        Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_CLIENT_ID, query)
                .map(collectionResult -> collectionResult.getTracks());

        return collectionResultObservable;
    }
}
