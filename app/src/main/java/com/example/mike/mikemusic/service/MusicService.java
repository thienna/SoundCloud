package com.example.mike.mikemusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by ThienNA on 02/08/2018.
 */

public class MusicService extends Service {

    // Action
    public static final String ACTION_CHANGE_MEDIA_STATE = "ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT_TRACK = "ACTION_NEXT_TRACK";
    public static final String ACTION_PREVIOUS_TRACK = "ACTION_PREVIOUS_TRACK";
    public static final String ACTION_OPEN_PLAY_MUSIC_ACTIVITY = "ACTION_OPEN_PLAY_MUSIC_ACTIVITY";

    //Binder
    private final IBinder mIBinder = new LocalBinder();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
