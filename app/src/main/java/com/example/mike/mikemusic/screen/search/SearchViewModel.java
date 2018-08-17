package com.example.mike.mikemusic.screen.search;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.data.repository.TrackRepository;
import com.example.mike.mikemusic.data.source.local.TrackLocalDataSource;
import com.example.mike.mikemusic.data.source.remote.TrackRemoteDataSource;
import com.example.mike.mikemusic.screen.BaseRecyclerViewViewModel;
import com.example.mike.mikemusic.screen.EndlessRecyclerViewOnScrollListener;
import com.example.mike.mikemusic.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ThienNA on 17/08/2018.
 */

public class SearchViewModel extends BaseRecyclerViewViewModel<Track, SearchAdapter> {
    private TrackRepository mTrackRepository;
    private CompositeDisposable mSubscription;
    private AppCompatActivity mActivity;
    private int mOffset;
    private String mQuery;

    public SearchViewModel(AppCompatActivity activity) {
        super(activity);
        mActivity = activity;
        mSubscription = new CompositeDisposable();
        mTrackRepository = TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                TrackLocalDataSource.getInstance(mActivity));
        mOffset = Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_OFFSET;
        mQuery = "";
    }

    public void searchTracks(String query, int offset) {
        if (!mQuery.equals(query)) {
            mAdapter.clearItem();
            mQuery = query;
            mOffset = Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_OFFSET;
            setLoadMoreListener();
        }
        Disposable subscribe = mTrackRepository.searchTrack(query, offset)
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
                            mAdapter.clearItem();
                            onDataLoadFailure(mActivity.getString(R.string.search_empty));
                        } else {
                            setEmptyViewVisible(View.GONE);
                            onDataLoaded(tracks);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        mAdapter.clearItem();
                        hideProgressBar();
                        onDataLoadFailure(mActivity.getString(R.string.no_internet));
                    }
                });
        mSubscription.add(subscribe);
    }

    @Override
    public SearchAdapter newAdapter() {
        return new SearchAdapter(mActivity, this, new ArrayList<>());
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
        setLoadMoreListener();
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    private void setLoadMoreListener(){
        setEndlessRecyclerViewOnScrollListener(new EndlessRecyclerViewOnScrollListener() {
            @Override
            public void onLoadMore() {
                mOffset += 10;
                searchTracks(mQuery, mOffset);
            }
        });
    }
}
