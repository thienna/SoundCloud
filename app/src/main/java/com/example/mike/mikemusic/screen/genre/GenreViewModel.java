package com.example.mike.mikemusic.screen.genre;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Genre;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.data.repository.TrackRepository;
import com.example.mike.mikemusic.data.source.local.TrackLocalDataSource;
import com.example.mike.mikemusic.data.source.remote.TrackRemoteDataSource;
import com.example.mike.mikemusic.screen.BaseRecyclerViewViewModel;
import com.example.mike.mikemusic.screen.EndlessRecyclerViewOnScrollListener;
import com.example.mike.mikemusic.service.MusicService;
import com.example.mike.mikemusic.utils.Constants;
import com.example.mike.mikemusic.utils.Utils;
import com.example.mike.mikemusic.utils.music.PlaybackInfoListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ThienNA on 05/08/2018.
 */

public class GenreViewModel extends BaseRecyclerViewViewModel<Track, TracksByGenreAdapter> {

    private TrackRepository mTrackRepository;
    private CompositeDisposable mSubscription;
    private List<Track> mTracks;
    private int mOffset;
    private Genre mGenre;

    //Service
    private MusicService mMusicService;
    private Intent mPlayIntent;
    private boolean mServiceBounded;
    //connect to the service
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            mMusicService = binder.getService();
            mServiceBounded = true;
            mMusicService.setPlaybackListener(new PlaybackListener());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBounded = false;
        }
    };

    public GenreViewModel(AppCompatActivity activity, Genre genre) {
        super(activity);
        mTrackRepository = TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                TrackLocalDataSource.getInstance(mActivity));
        mSubscription = new CompositeDisposable();
        mTracks = new ArrayList<>();
        mOffset = Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_OFFSET;
        mGenre = genre;
    }

    @Override
    public TracksByGenreAdapter newAdapter() {
        return new TracksByGenreAdapter(mActivity, this, new ArrayList<>());
    }

    @Override
    public void onRecyclerViewItemClick(Track dataItem) {
        Toast.makeText(mActivity, dataItem.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataLoaded(List<Track> data) {
        mAdapter.addItem(data);
        mTracks.addAll(data);
    }

    @Override
    public void onStart() {
        setLoadMoreListener();
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(mActivity, MusicService.class);
            mActivity.startService(mPlayIntent);
            mActivity.bindService(mPlayIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    public void onBackPress() {
        mActivity.onBackPressed();
    }

    private void loadTracksByGenre(String genrePath, int offset) {
        Disposable subscribe = mTrackRepository.getTracksByGenre(genrePath, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showProgressBar())
                .subscribe(tracks -> {
                            hideProgressBar();
                            onDataLoaded(tracks);
                        }, throwable -> {
                            onDataLoadFailure(mActivity.getString(R.string.no_internet));
                            hideProgressBar();
                        }
                );
        mSubscription.add(subscribe);
    }

    private void setLoadMoreListener() {
        loadTracksByGenre(mGenre.getPath(), mOffset);
        setEndlessRecyclerViewOnScrollListener(new EndlessRecyclerViewOnScrollListener() {
            @Override
            public void onLoadMore() {
                mOffset += 10;
                loadTracksByGenre(mGenre.getPath(), mOffset);
            }
        });
    }

    public void onFloatButtonClick() {
        if (mTracks == null || mTracks.size() == 0) {
            Toast.makeText(mMusicService, "Nothing to play!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mServiceBounded) {
            mMusicService.playTracksList(mTracks, 0);
        }

    }

    public String formatDuration(long number) {
        return Utils.parseMilliSecondsToTimer(number);
    }

    public class PlaybackListener extends PlaybackInfoListener {
        public void onProgressUpdate(double percent) {
            Log.d("TAG", "onProgressUpdate: " + percent);
        }

        public void onTrackChanged(Track track) {
            Log.d("TAG", "onTrackChanged: " + track);
        }

        public void onStateChanged(int state) {
            Log.d("TAG", "onStateChanged: " + state);
        }
    }
}
