package com.example.mike.mikemusic;

import android.app.Application;

import com.example.mike.mikemusic.data.source.api.service.SoundCloudServiceClient;

/**
 * Created by ThienNA on 01/08/2018.
 */

public class MainApplication extends Application {

    private static MainApplication sInstance;

    public static MainApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoundCloudServiceClient.initialize(this);
        sInstance = this;
    }
}
