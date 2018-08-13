package com.example.mike.mikemusic.screen.playmusic;

import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ThienNA on 04/08/2018.
 */

public class PlayMusicViewModel extends BaseObservable {
    private PlayMusicActivity mMainActivity;

    public PlayMusicViewModel(AppCompatActivity activity) {
        mMainActivity = (PlayMusicActivity) activity;
    }

    public void onBackIconClick() {
        mMainActivity.onBackPressed();
    }

    public void onNowPlayingIconClick() {
        // TODO: 8/6/2018 - Toggle Now Playing Bottom Sheet
    }
}
