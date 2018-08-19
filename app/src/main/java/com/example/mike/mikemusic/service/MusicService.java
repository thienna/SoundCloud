package com.example.mike.mikemusic.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.screen.main.MainActivity;
import com.example.mike.mikemusic.utils.music.MusicPlayerController;
import com.example.mike.mikemusic.utils.music.MusicPlayerManager;
import com.example.mike.mikemusic.utils.music.PlaybackInfoListener;

import java.util.List;

/**
 * Created by ThienNA on 02/08/2018.
 */

public class MusicService extends Service implements MusicPlayerManager {

    // Action
    public static final String ACTION_CHANGE_MEDIA_STATE = "ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT_TRACK = "ACTION_NEXT_TRACK";
    public static final String ACTION_PREVIOUS_TRACK = "ACTION_PREVIOUS_TRACK";
    public static final String ACTION_OPEN_PLAY_MUSIC_ACTIVITY = "ACTION_OPEN_PLAY_MUSIC_ACTIVITY";

    // Notification
    private static final int NOTIFY_ID = 1;
    private static final int ORDER_ACTION_PREVIOUS = 0;
    private static final int ORDER_ACTION_PLAY_PAUSE = 1;
    private static final int ORDER_ACTION_NEXT = 2;
    private static final String TITLE_ACTION_PREVIOUS = "Previous";
    private static final String TITLE_ACTION_PLAY = "Play";
    private static final String TITLE_ACTION_PAUSE = "Pause";
    private static final String TITLE_ACTION_NEXT = "Next";

    //Binder
    private final IBinder mIBinder = new MusicBinder();

    private MusicPlayerManager mMusicPlayerManager;
    private NotificationCompat.Builder mBuilder;
    private PendingIntent mPendingIntentOpenApp;
    private PendingIntent mPendingIntentNext;
    private PendingIntent mPendingIntentPrev;
    private PendingIntent mPendingIntentPlayPause;
    private Bitmap mBitmap;

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicPlayerManager = new MusicPlayerController(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        handleIntent(intent);
        return mIBinder;
    }


    @Override
    public int getMediaState() {
        if (mMusicPlayerManager == null) {
            return PlaybackInfoListener.State.INVALID;
        }
        return mMusicPlayerManager.getMediaState();
    }

    @Override
    public void changeMediaState() {
        if (mMusicPlayerManager == null) {
            return;
        }
        mMusicPlayerManager.changeMediaState();
    }

    @Override
    public void playPreviousTrack() {
        if (mMusicPlayerManager == null) {
            return;
        }
        mMusicPlayerManager.playPreviousTrack();
    }

    @Override
    public void playNextTrack() {
        if (mMusicPlayerManager == null) {
            return;
        }
        mMusicPlayerManager.playNextTrack();
    }

    @Override
    public boolean isShuffle() {
        return mMusicPlayerManager != null && mMusicPlayerManager.isShuffle();
    }

    @Override
    public void toggleShuffleState() {
        if (mMusicPlayerManager == null) {
            return;
        }
        mMusicPlayerManager.toggleShuffleState();
    }

    @Override
    public int getLoopType() {
        if (mMusicPlayerManager == null) return PlaybackInfoListener.LoopType.NO_LOOP;
        return mMusicPlayerManager.getLoopType();
    }

    @Override
    public void changeLoopType() {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.changeLoopType();
    }

    // TODO: 8/15/2018 Test this method
    @Override
    public int getCurrentTrackPosition() {
        return mMusicPlayerManager != null ? mMusicPlayerManager.getCurrentTrackPosition() : 0;
    }

    @Override
    public void addToNextUp(Track track) {
        if (mMusicPlayerManager == null) {
            return;
        }
        mMusicPlayerManager.addToNextUp(track);
    }

    // TODO: 8/15/2018 Test this method
    @Override
    public void playTrackAtPosition(int pos, Track... tracks) {
        if (mMusicPlayerManager == null) {
            mMusicPlayerManager = new MusicPlayerController(this);
        }
        mMusicPlayerManager.playTrackAtPosition(pos, tracks);
    }

    @Override
    public List<Track> getTracks() {
        if (mMusicPlayerManager == null) {
            return null;
        }
        return mMusicPlayerManager.getTracks();
    }

    @Override
    public Track getCurrentTrack() {
        return mMusicPlayerManager != null ? mMusicPlayerManager.getCurrentTrack() : null;
    }

    @Override
    public void seekTo(int position) {
        if (mMusicPlayerManager == null) {
            return;
        }
        mMusicPlayerManager.seekTo(position);
    }

    @Override
    public void addPlaybackInfoListener(PlaybackInfoListener listener) {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.addPlaybackInfoListener(listener);
    }

    @Override
    public void removePlaybackInfoListener(PlaybackInfoListener listener) {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.removePlaybackInfoListener(listener);
    }

    @Override
    public void release() {
    }

    @Override
    public void startProgressCallback() {
    }

    @Override
    public void endProgressCallback() {
    }

    public void handleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action == null) return;
        switch (action) {
            case ACTION_CHANGE_MEDIA_STATE:
                if (getMediaState() != PlaybackInfoListener.State.PREPARE) {
                    changeMediaState();
                }
                break;
            case ACTION_PREVIOUS_TRACK:
                playPreviousTrack();
                break;
            case ACTION_NEXT_TRACK:
                playNextTrack();
                break;
        }
    }

    public void loadImage() {
        if (getCurrentTrack() == null) {
            return;
        }
        Glide.with(this).asBitmap().load(getCurrentTrack().getArtworkUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mBitmap = resource;

                        mBuilder.setLargeIcon(mBitmap);
                        NotificationManager notificationManager = (NotificationManager)
                                getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(NOTIFY_ID, mBuilder.build());
                    }
                });
    }

    public void initNotification(@PlaybackInfoListener.State int state) {
        if (getCurrentTrack() == null) {
            return;
        }
        initBaseNotification();
        if (state == PlaybackInfoListener.State.PAUSE) {
            stopForeground(false);
            mBuilder.setOngoing(false)
                    .addAction(R.drawable.ic_skip_previous_black_36dp, TITLE_ACTION_PREVIOUS, mPendingIntentPrev)
                    .addAction(R.drawable.ic_play_arrow_black_48dp, TITLE_ACTION_PLAY, mPendingIntentPlayPause)
                    .addAction(R.drawable.ic_skip_next_black_36dp, TITLE_ACTION_NEXT, mPendingIntentNext)
                    .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(ORDER_ACTION_PREVIOUS,
                                    ORDER_ACTION_PLAY_PAUSE, ORDER_ACTION_NEXT));
            NotificationManager notificationManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, mBuilder.build());
        } else {
            mBuilder.addAction(R.drawable.ic_skip_previous_black_36dp, TITLE_ACTION_PREVIOUS, mPendingIntentPrev)
                    .addAction(R.drawable.ic_pause_black_48dp, TITLE_ACTION_PAUSE, mPendingIntentPlayPause)
                    .addAction(R.drawable.ic_skip_next_black_36dp, TITLE_ACTION_NEXT, mPendingIntentNext)
                    .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(ORDER_ACTION_PREVIOUS, ORDER_ACTION_PLAY_PAUSE, ORDER_ACTION_NEXT));

            startForeground(NOTIFY_ID, mBuilder.build());
        }
    }

    private void setLargeIconBuilder() {
        if (mBuilder == null) {
            return;
        }
        if (mBitmap == null) {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher_foreground));
        } else {
            mBuilder.setLargeIcon(mBitmap);
        }
    }

    private void initBaseNotification() {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setAction(ACTION_OPEN_PLAY_MUSIC_ACTIVITY);
        mPendingIntentOpenApp = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);

        Intent actionNextIntent = new Intent(getApplicationContext(), MusicService.class);
        actionNextIntent.setAction(ACTION_NEXT_TRACK);
        mPendingIntentNext = PendingIntent.getService(getApplicationContext(), 0,
                actionNextIntent, 0);

        Intent actionPrevIntent = new Intent(getApplicationContext(), MusicService.class);
        actionPrevIntent.setAction(ACTION_PREVIOUS_TRACK);
        mPendingIntentPrev = PendingIntent.getService(getApplicationContext(), 0,
                actionPrevIntent, 0);

        Intent actionPlayIntent = new Intent(getApplicationContext(), MusicService.class);
        actionPlayIntent.setAction(ACTION_CHANGE_MEDIA_STATE);
        mPendingIntentPlayPause = PendingIntent.getService(getApplicationContext(), 0,
                actionPlayIntent, 0);

        // TODO: 8/15/2018 test getUser()
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(getCurrentTrack().getTitle())
                .setContentText(getCurrentTrack().getUser().getUserName())
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_music_note_green_24dp)
                .setContentIntent(mPendingIntentOpenApp);

        setLargeIconBuilder();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
