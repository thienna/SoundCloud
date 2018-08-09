package com.example.mike.mikemusic.data.source.api.service;

import com.example.mike.mikemusic.data.model.CollectionResult;
import com.example.mike.mikemusic.utils.Constants;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ThienNA on 08/08/2018.
 */

public interface ApiSoundCloud {
    @GET("/charts")
    Observable<CollectionResult> getTracksByGenre(
            @Query(Constants.ApiSoundCloud.PARAM_KEY_GENRE) String genre,
            @Query(Constants.ApiSoundCloud.PARAM_KEY_OFFSET) int offset,
            @Query(Constants.ApiSoundCloud.PARAM_KEY_KIND) String kind,
            @Query(Constants.ApiSoundCloud.PARAM_KEY_LIMIT) int limit,
            @Query(Constants.ApiSoundCloud.PARAM_KEY_CLIENT_ID) String clientId);
}
