package com.example.mike.mikemusic.screen.personal;

import android.support.v7.app.AppCompatActivity;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.data.repository.TrackRepository;
import com.example.mike.mikemusic.data.source.local.TrackLocalDataSource;
import com.example.mike.mikemusic.data.source.remote.TrackRemoteDataSource;
import com.example.mike.mikemusic.screen.BaseRecyclerViewViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ThienNA on 08/08/2018.
 */

public class PersonalViewModel extends BaseRecyclerViewViewModel<Track, TrackOfflineRecyclerAdapter> {

    private TrackRepository mTrackRepository;
    private CompositeDisposable mSubscription;
    private AppCompatActivity mActivity;

    public PersonalViewModel(AppCompatActivity activity) {
        super(activity);
        mActivity = activity;
        mSubscription = new CompositeDisposable();
        mTrackRepository = TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                TrackLocalDataSource.getInstance(mActivity));
    }

    private void getTracksLocal() {
        Disposable subscribe = mTrackRepository.getTracksLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        showProgressBar();
                    }
                })
                .subscribe(new Consumer<List<Track>>() {
                    @Override
                    public void accept(List<Track> tracks) {
                        hideProgressBar();
                        if (tracks.size() == 0) {
                            onDataLoadFailure(mActivity.getString(R.string.empty_recycler));
                        } else {
                            onDataLoaded(tracks);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        hideProgressBar();
                        // TODO: 8/7/2018 Show Error
                    }
                });
        mSubscription.add(subscribe);
    }

    @Override
    public TrackOfflineRecyclerAdapter newAdapter() {
        return new TrackOfflineRecyclerAdapter(mActivity, this, new ArrayList<>());
    }

    @Override
    public void onRecyclerViewItemClick(Track dataItem) {
    }

    @Override
    public void onDataLoaded(List<Track> data) {
        mAdapter.addItem(data);
    }

    @Override
    public void onStart() {
        getTracksLocal();
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }
}
