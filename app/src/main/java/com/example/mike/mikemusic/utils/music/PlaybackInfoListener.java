package com.example.mike.mikemusic.utils.music;

import android.support.annotation.IntDef;

import com.example.mike.mikemusic.data.model.Track;

/**
 * Created by ThienNA on 14/08/2018.
 */

public abstract class PlaybackInfoListener {

    private boolean mUpdatingProgressSeekBar;

    protected PlaybackInfoListener() {
    }

    protected PlaybackInfoListener(boolean updatingProgressSeekBar) {
        mUpdatingProgressSeekBar = updatingProgressSeekBar;
    }

    public void onProgressUpdate(double percent) {
    }

    public void onTrackChanged(Track track) {
    }

    public void onStateChanged(@State int state) {
    }

    boolean isUpdatingProgressSeekBar() {
        return mUpdatingProgressSeekBar;
    }

    @IntDef({State.COMPLETED, State.INVALID, State.PAUSE,
            State.PLAYING, State.PREPARE})
    public @interface State {
        int INVALID = -1;
        int PLAYING = 0;
        int PAUSE = 1;
        int PREPARE = 2;
        int COMPLETED = 3;
    }

    @IntDef({LoopType.NO_LOOP, LoopType.LOOP_ONE, LoopType.LOOP_LIST})
    public @interface LoopType {
        int NO_LOOP = -1;
        int LOOP_ONE = 0;
        int LOOP_LIST = 1;
    }
}
