package com.example.mike.mikemusic.screen.genre;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Genre;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.data.repository.TrackRepository;
import com.example.mike.mikemusic.data.source.local.TrackLocalDataSource;
import com.example.mike.mikemusic.data.source.remote.TrackRemoteDataSource;
import com.example.mike.mikemusic.screen.BaseRecyclerViewViewModel;
import com.example.mike.mikemusic.screen.EndlessRecyclerViewOnScrollListener;
import com.example.mike.mikemusic.screen.playercontrol.PlayerControlFragment;
import com.example.mike.mikemusic.utils.Constants;
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
    private List<Track> mTracks = new ArrayList<>();
    private int mOffset = Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_OFFSET;
    private Genre mGenre;
    private PlayerControlFragment mPlayerControlFragment;

    GenreViewModel(AppCompatActivity activity, Genre genre) {
        super(activity);

        mTrackRepository = TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                TrackLocalDataSource.getInstance(mActivity));
        mSubscription = new CompositeDisposable();
        mGenre = genre;

        addPlayerControlFragment();
    }

    private void addPlayerControlFragment() {
        mPlayerControlFragment = PlayerControlFragment.newInstance();
        mActivity.getSupportFragmentManager().beginTransaction().add(R.id
                .fragment_player_control, mPlayerControlFragment).commit();
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
        if (mPlayerControlFragment == null) {
            return;
        }

        mPlayerControlFragment.playTrackList(mTracks);
    }

    public String formatDuration(long number) {
        return Utils.parseMilliSecondsToTimer(number);
    }
}
