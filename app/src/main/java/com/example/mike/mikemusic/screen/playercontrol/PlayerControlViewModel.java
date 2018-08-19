package com.example.mike.mikemusic.screen.playercontrol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.databinding.FragmentPlayerControlBinding;
import com.example.mike.mikemusic.screen.BaseViewModel;
import com.example.mike.mikemusic.service.MusicService;
import com.example.mike.mikemusic.utils.music.PlaybackInfoListener;

/**
 * Created by ThienNA on 07/08/2018.
 */

public class PlayerControlViewModel implements BaseViewModel {

    private AppCompatActivity mActivity;
    private FragmentPlayerControlBinding mBinding;
    private PlaybackListener mListener = new PlaybackListener();

    //Service
    private MusicService mMusicService;
    private Intent mPlayIntent;
    private boolean mServiceBounded;
    private MusicService.MusicBinder mMusicBinder;

    //connect to the service
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBinder = (MusicService.MusicBinder) service;
            //get service
            mMusicService = mMusicBinder.getService();
            mMusicService.addPlaybackInfoListener(mListener);
            mServiceBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBinder = null;
        }
    };

    public PlayerControlViewModel(AppCompatActivity activity, FragmentPlayerControlBinding binding) {
        mActivity = activity;
        mBinding = binding;
    }

    @Override
    public void onStart() {
        doBindService();
    }

    @Override
    public void onStop() {
        doUnbindService();
    }

    public void doBindService() {
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(mActivity, MusicService.class);
        }
        mActivity.startService(mPlayIntent);
        if (mActivity.bindService(mPlayIntent, mServiceConnection, Context.BIND_AUTO_CREATE)) {
            mServiceBounded = true;
        }
    }

    public void doUnbindService() {
        if (mServiceBounded) {
            mActivity.unbindService(mServiceConnection);
            mMusicService.removePlaybackInfoListener(mListener);
            mServiceBounded = false;
        }
    }

    public class PlaybackListener extends PlaybackInfoListener {
        public void onProgressUpdate(double percent) {
        }

        public void onTrackChanged(Track track) {
            Log.d("AMEN", "onTrackChanged: " + track.getTitle());
            mBinding.setTrack(track);
        }

        public void onStateChanged(int state) {
            Log.d("TAG", "onStateChanged: " + state);
        }
    }
}
