package com.example.mike.mikemusic.utils.music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import com.example.mike.mikemusic.BuildConfig;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.service.MusicService;
import com.example.mike.mikemusic.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ThienNA on 14/08/2018.
 */

public class MusicPlayerController implements MusicPlayerManager,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final int PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 1000;
    private static final int MSG_UPDATE_SEEK_BAR_POSITION = 0;

    private MusicService mMusicService;
    private ScheduledExecutorService mScheduledExecutorService;
    private Runnable mSeekBarPositionUpdateTask;
    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private List<PlaybackInfoListener> mListeners = new ArrayList<>();
    private int mCurrentTrackPosition;
    @PlaybackInfoListener.State
    private int mState;
    @PlaybackInfoListener.LoopType
    private int mLoopType = PlaybackInfoListener.LoopType.NO_LOOP;
    private boolean mIsShuffle;
    private List<Track> mTracksOrigin;
    private List<Track> mTracks;


    public MusicPlayerController(MusicService musicService) {
        mMusicService = musicService;
    }

    @Override
    public void release() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void changeMediaState() {
        if (mMediaPlayer == null) {
            return;
        }

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            notifyChangingState(PlaybackInfoListener.State.PAUSE);
        } else {
            mMediaPlayer.start();
            notifyChangingState(PlaybackInfoListener.State.PLAYING);

            if (mListeners == null || mListeners.isEmpty()) {
                return;
            }

//            for (PlaybackInfoListener listener : mListeners) {
//                if (listener.isUpdatingProgressSeekBar() && mScheduledExecutorService == null) {
//                    startProgressCallback();
//                }
//            }
        }
    }

    @Override
    public void playNextTrack() {
        if (mCurrentTrackPosition == mTracks.size() - 1) {
            if (mLoopType != PlaybackInfoListener.LoopType.LOOP_LIST) {
                return;
            }
            mCurrentTrackPosition = -1;
        }

        mCurrentTrackPosition++;
        prepareLoadTrack();
    }

    @Override
    public void playPreviousTrack() {
        if (mCurrentTrackPosition == 0) {
            return;
        }
        mCurrentTrackPosition--;
        prepareLoadTrack();
    }

    @Override
    public void startProgressCallback() {
        if (mScheduledExecutorService == null) {
            mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        if (mSeekBarPositionUpdateTask == null) {
            mHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == MSG_UPDATE_SEEK_BAR_POSITION) {
                        updateProgressCallBackTask();
                        return true;
                    }
                    return false;
                }
            });

            mSeekBarPositionUpdateTask = new Runnable() {
                @Override
                public void run() {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(MSG_UPDATE_SEEK_BAR_POSITION);
                    }
                }
            };
        }

        mScheduledExecutorService.scheduleAtFixedRate(mSeekBarPositionUpdateTask, 0,
                PLAYBACK_POSITION_REFRESH_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void endProgressCallback() {
//        if (mScheduledExecutorService == null) {
//            return;
//        }
//
//        mScheduledExecutorService.shutdownNow();
//        mScheduledExecutorService = null;
//        mSeekBarPositionUpdateTask = null;

//        if (mListener == null) {
//            return;
//        }
//
//        mListener.onProgressUpdate(0);
    }

    @Override
    public void addPlaybackInfoListener(PlaybackInfoListener listener) {
        if (listener == null) {
            return;
        }
        mListeners.add(listener);
        if (mMediaPlayer == null) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            listener.onTrackChanged(mTracks.get(mCurrentTrackPosition));
        }
//        if (mListener != null && mListener.isUpdatingProgressSeekBar()) {
//            startProgressCallback();
//        }
    }

    @Override
    public void removePlaybackInfoListener(PlaybackInfoListener listener) {
        if (listener == null) {
            return;
        }
        mListeners.remove(listener);
    }

    @Override
    public void seekTo(int percent) {
        if (getMediaState() == PlaybackInfoListener.State.PAUSE || getMediaState() ==
                PlaybackInfoListener.State.PLAYING) {
            mMediaPlayer.seekTo(mMediaPlayer.getDuration() / Constants.SEEK_BAR_TRACK_DURATION *
                    percent);
        }
    }

    @Override
    public Track getCurrentTrack() {
        return mTracks != null ? mTracks.get(mCurrentTrackPosition) : null;
    }

    @Override
    public int getMediaState() {
        return mState;
    }

    @Override
    public List<Track> getTracks() {
        return mTracks;
    }

    @Override
    public void playTrackAtPosition(int position, Track... tracks) {
        if (tracks == null && mTracks == null) {
            notifyChangingState(PlaybackInfoListener.State.INVALID);
            return;
        }

        if ((tracks == null || tracks.length == 0) && mCurrentTrackPosition == position) {
            return;
        }

        if (tracks != null && tracks.length != 0) {
            mTracks = new ArrayList<>();
            Collections.addAll(mTracks, tracks);

            mIsShuffle = false;
        }

        mCurrentTrackPosition = position;
        prepareLoadTrack();
    }

    @Override
    public void addToNextUp(Track track) {
        if (mTracks == null || mTracks.isEmpty()) return;
        mTracks.add(track);

        if (!mIsShuffle) return;
        mTracksOrigin.add(track);
    }

    @Override
    public int getCurrentTrackPosition() {
        return mCurrentTrackPosition;
    }

    @Override
    public int getLoopType() {
        return mLoopType;
    }

    @Override
    public void changeLoopType() {
        switch (mLoopType) {
            case PlaybackInfoListener.LoopType.NO_LOOP:
                mLoopType = PlaybackInfoListener.LoopType.LOOP_LIST;
                break;
            case PlaybackInfoListener.LoopType.LOOP_LIST:
                mLoopType = PlaybackInfoListener.LoopType.LOOP_ONE;
                break;
            case PlaybackInfoListener.LoopType.LOOP_ONE:
                mLoopType = PlaybackInfoListener.LoopType.NO_LOOP;
                break;
        }
    }

    @Override
    public boolean isShuffle() {
        return mIsShuffle;
    }

    @Override
    public void toggleShuffleState() {
        if (!mIsShuffle) {
            mTracksOrigin = new ArrayList<>();
            mTracksOrigin.addAll(mTracks);

            Track currentTrack = mTracks.get(mCurrentTrackPosition);

            Collections.shuffle(mTracks);
            mCurrentTrackPosition = mTracks.indexOf(currentTrack);

            mIsShuffle = true;
            return;
        }

        Track currentTrack = mTracks.get(mCurrentTrackPosition);

        mTracks = new ArrayList<>();
        mTracks.addAll(mTracksOrigin);

        mCurrentTrackPosition = mTracks.indexOf(currentTrack);

        mIsShuffle = false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        endProgressCallback();
        notifyChangingState(PlaybackInfoListener.State.PAUSE);
        handleCompletionWithLoopType();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        notifyChangingState(PlaybackInfoListener.State.PLAYING);

//        if (mListener == null) {
//            return;
//        }

//        if (mListener.isUpdatingProgressSeekBar()) {
//            startProgressCallback();
//        }
    }

    private void notifyChangingState(@PlaybackInfoListener.State int state) {
        mState = state;
        if (mMusicService != null) {
            if (state == PlaybackInfoListener.State.PREPARE) mMusicService.loadImage();
            mMusicService.initNotification(state);
        }

        if (mListeners == null) return;
        for (PlaybackInfoListener listener : mListeners) {
            listener.onStateChanged(mState);
        }
    }

    private void prepareLoadTrack() {
        if (mTracks == null || mTracks.isEmpty()) {
            notifyChangingState(PlaybackInfoListener.State.INVALID);
            return;
        }

        endProgressCallback();
        release();

        notifyChangingState(PlaybackInfoListener.State.PREPARE);
        loadTrack();

        if (mListeners == null || mListeners.isEmpty()) {
            return;
        }

        for (PlaybackInfoListener listener : mListeners) {
            listener.onTrackChanged(mTracks.get(mCurrentTrackPosition));
        }
    }

    private void loadTrack() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);

        try {
            mMediaPlayer.setDataSource(mTracks.get(mCurrentTrackPosition).getUri() +
                    "/stream?client_id=" + BuildConfig.API_KEY);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            notifyChangingState(PlaybackInfoListener.State.INVALID);
            if (mCurrentTrackPosition < mTracks.size()) {
                playNextTrack();
            }
        }
    }

    private void handleCompletionWithLoopType() {
        switch (mLoopType) {
            case PlaybackInfoListener.LoopType.NO_LOOP:
                break;
            case PlaybackInfoListener.LoopType.LOOP_ONE:
                onPrepared(mMediaPlayer);
                break;
            case PlaybackInfoListener.LoopType.LOOP_LIST:
                if (mCurrentTrackPosition != mTracks.size() - 1) {
                    break;
                }
                mCurrentTrackPosition = -1;
                break;
        }

        playNextTrack();
    }

    private void updateProgressCallBackTask() {
//        if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) {
//            return;
//        }
//        int currentPosition = mMediaPlayer.getCurrentPosition();
//        if (mListener == null) {
//            return;
//        }
//        mListener.onProgressUpdate((double) currentPosition / mMediaPlayer.getDuration() *
//                Constants.SEEK_BAR_TRACK_DURATION);
    }
}
