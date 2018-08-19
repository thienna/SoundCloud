package com.example.mike.mikemusic.screen.playercontrol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.Bindable;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.databinding.FragmentPlayerControlBinding;
import com.example.mike.mikemusic.screen.BaseViewModel;
import com.example.mike.mikemusic.service.MusicService;
import com.example.mike.mikemusic.utils.music.PlaybackInfoListener;

import java.util.List;

/**
 * Created by ThienNA on 07/08/2018.
 */

public class PlayerControlViewModel implements BaseViewModel {

    private AppCompatActivity mActivity;
    private FragmentPlayerControlBinding mBinding;
    private PlaybackListener mListener = new PlaybackListener();
    private Track mTrack;

    //Service
    private MusicService mMusicService;
    private boolean mServiceBounded;

    //connect to the service
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getService();
            mServiceBounded = true;
            mMusicService.addPlaybackInfoListener(mListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBounded = false;
        }
    };

    PlayerControlViewModel(AppCompatActivity activity, FragmentPlayerControlBinding binding) {
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

    public void onClick(View view) {
        if (!mServiceBounded || mTrack == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.image_player_control_next:
                mMusicService.playNextTrack();
                break;
            case R.id.image_player_control_back:
                mMusicService.playPreviousTrack();
                break;
            case R.id.image_player_control_play:
                mMusicService.changeMediaState();
                break;
        }
    }

    public void playTrackList(List<Track> tracks) {
        if (tracks == null || tracks.isEmpty()) {
            Toast.makeText(mActivity, "Nothing to play!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mServiceBounded) {
            mMusicService.playTrackAtPosition(0, tracks.toArray(new Track[tracks.size()]));
        }
    }

    public class PlaybackListener extends PlaybackInfoListener {
        public void onProgressUpdate(double percent) {
        }

        public void onTrackChanged(Track track) {
            if (!mServiceBounded) {
                return;
            }

            mBinding.setTrack(track);
            mTrack = track;
        }

        public void onStateChanged(int state) {
            Toast.makeText(mActivity, "State: " + state, Toast.LENGTH_SHORT).show();
        }
    }

    private void doBindService() {
        Intent intent = new Intent(mActivity, MusicService.class);
        if (mActivity.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)) {
            mServiceBounded = true;
        }
    }

    private void doUnbindService() {
        if (mServiceBounded) {
            mMusicService.removePlaybackInfoListener(mListener);
            mActivity.unbindService(mServiceConnection);
            mServiceBounded = false;
        }
    }
}
