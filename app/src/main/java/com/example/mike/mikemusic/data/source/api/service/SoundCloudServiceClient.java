package com.example.mike.mikemusic.data.source.api.service;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.mike.mikemusic.utils.Constants;

/**
 * Created by ThienNA on 09/08/2018.
 */

public class SoundCloudServiceClient extends ServiceClient {

    private static ApiSoundCloud sApi;

    public static void initialize(@NonNull Application application) {
        sApi = createService(application, Constants.ApiSoundCloud.BASE_URL, ApiSoundCloud
                .class);
    }

    public static ApiSoundCloud getInstance() {
        if (sApi == null) {
            throw new RuntimeException("Need call method SoundCloudServiceClient #initialize() " +
                    "first");
        }
        return sApi;
    }
}
