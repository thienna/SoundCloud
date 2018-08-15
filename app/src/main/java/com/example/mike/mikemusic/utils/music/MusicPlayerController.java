package com.example.mike.mikemusic.utils.music;

import android.media.MediaPlayer;

import com.example.mike.mikemusic.data.model.Track;

import java.util.List;

/**
 * Created by ThienNA on 14/08/2018.
 */

public class MusicPlayerController implements MusicPlayerManager,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{
    @Override
    public void release() {

    }

    @Override
    public void changeMediaState() {

    }

    @Override
    public void playNextTrack() {

    }

    @Override
    public void playPreviousTrack() {

    }

    @Override
    public void startProgressCallback() {

    }

    @Override
    public void endProgressCallback() {

    }

    @Override
    public void setPlaybackInfoListener(PlaybackInfoListener listener) {

    }

    @Override
    public void seekTo(int percent) {

    }

    @Override
    public Track getCurrentTrack() {
        return null;
    }

    @Override
    public int getCurrentState() {
        return 0;
    }

    @Override
    public List<Track> getListTrack() {
        return null;
    }

    @Override
    public void playTrackAtPosition(int position, Track... tracks) {

    }

    @Override
    public void addToNextUp(Track track) {

    }

    @Override
    public int getCurrentTrackPosition() {
        return 0;
    }

    @Override
    public int getLoopType() {
        return 0;
    }

    @Override
    public void changeLoopType() {

    }

    @Override
    public boolean isShuffle() {
        return false;
    }

    @Override
    public void changeShuffleState() {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
