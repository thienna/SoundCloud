package com.example.mike.mikemusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.example.mike.mikemusic.BuildConfig;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.utils.music.PlaybackInfoListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThienNA on 02/08/2018.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    // Action
    public static final String ACTION_CHANGE_MEDIA_STATE = "ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT_TRACK = "ACTION_NEXT_TRACK";
    public static final String ACTION_PREVIOUS_TRACK = "ACTION_PREVIOUS_TRACK";
    public static final String ACTION_OPEN_PLAY_MUSIC_ACTIVITY = "ACTION_OPEN_PLAY_MUSIC_ACTIVITY";
    //Binder
    private final IBinder mIBinder = new MusicBinder();
    // Notification
    private static final int NOTIFY_ID = 1;
    private static final int ORDER_ACTION_PREVIOUS = 0;
    private static final int ORDER_ACTION_PLAY_PAUSE = 1;
    private static final int ORDER_ACTION_NEXT = 2;
    private static final String TITLE_ACTION_PREVIOUS = "Previous";
    private static final String TITLE_ACTION_PLAY = "Play";
    private static final String TITLE_ACTION_PAUSE = "Pause";
    private static final String TITLE_ACTION_NEXT = "Next";

//    private MusicPlayerManager mMusicPlayerManager;
//    private NotificationCompat.Builder mBuilder;
//    private PendingIntent mPendingIntentOpenApp;
//    private PendingIntent mPendingIntentNext;
//    private PendingIntent mPendingIntentPrev;
//    private PendingIntent mPendingIntentPlayPause;
//    private Bitmap mBitmap;

    private MediaPlayer mMediaPlayer;
    private List<Track> mTracks;

    private int mCurrentTrackPosition;

    private PlaybackInfoListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();
        initMusicPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        handleIntent(intent);
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    public void initMusicPlayer() {
        mCurrentTrackPosition = 0;
        mMediaPlayer = new MediaPlayer();
        mTracks = new ArrayList<>();

        //set player properties
        mMediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        if (mListener != null) {
            mListener.onTrackChanged(mTracks.get(mCurrentTrackPosition));
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public void setList(ArrayList<Track> tracks) {
        if (tracks == null || tracks.size() == 0) return;
        mTracks.addAll(tracks);
    }

    public void handleIntent(Intent intent) {

    }

    public void setPlaybackListener(PlaybackInfoListener listener) {
        if (listener == null) {
            return;
        }
        mListener = listener;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mListener.onTrackChanged(mTracks.get(mCurrentTrackPosition));
        }
    }

    public void playTracksList(List<Track> tracks, int position) {
        if (tracks == null || tracks.size() == 0) return;
        if (mMediaPlayer == null) {
            initMusicPlayer();
            mCurrentTrackPosition = position;
            mTracks.addAll(tracks);
            playMusic();

            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
            mTracks.clear();
            mTracks.addAll(tracks);
            mCurrentTrackPosition = position;
            playMusic();
            return;
        }

        mCurrentTrackPosition = position;
        mTracks.addAll(tracks);
        playMusic();
    }

    private void playMusic() {
        if (mMediaPlayer != null){
            mMediaPlayer.reset();
        }

        if (mTracks == null || mTracks.size() == 0) return;
        try {
            mMediaPlayer.setDataSource(mTracks.get(mCurrentTrackPosition).getUri() +
                    "/stream?client_id=" + BuildConfig.API_KEY);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
