package com.example.mike.mikemusic.screen.genre;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.data.repository.TrackRepository;
import com.example.mike.mikemusic.data.source.remote.TrackRemoteDataSource;
import com.example.mike.mikemusic.screen.BaseRecyclerViewViewModel;
import com.example.mike.mikemusic.utils.Utils;

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

    public GenreViewModel(AppCompatActivity activity) {
        super(activity);
        mTrackRepository = TrackRepository.getInstance(TrackRemoteDataSource.getInstance());
        mSubscription = new CompositeDisposable();
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
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    public void onBackPress() {
        mActivity.onBackPressed();
    }

    public void loadTracksByGenre(String genrePath, int offset) {
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

    public String formatDuration(long number) {
        return Utils.parseMilliSecondsToTimer(number);
    }
}
