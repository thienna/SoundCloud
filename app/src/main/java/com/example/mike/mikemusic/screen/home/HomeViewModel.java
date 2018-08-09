package com.example.mike.mikemusic.screen.home;

import android.support.v7.app.AppCompatActivity;

import com.example.mike.mikemusic.data.model.Genre;
import com.example.mike.mikemusic.data.repository.GenreRepository;
import com.example.mike.mikemusic.data.source.local.GenreLocalDataSource;
import com.example.mike.mikemusic.screen.BaseRecyclerViewViewModel;
import com.example.mike.mikemusic.screen.genre.GenreActivity;
import com.example.mike.mikemusic.screen.playmusic.PlayMusicActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ThienNA on 03/08/2018.
 */

public class HomeViewModel extends BaseRecyclerViewViewModel<Genre, GenreAdapter> {

    private GenreRepository mGenreRepository;
    private CompositeDisposable mSubscription;
    private AppCompatActivity mActivity;

    public HomeViewModel(AppCompatActivity activity) {
        super(activity);
        mActivity = activity;
        mGenreRepository = GenreRepository.getInstance(GenreLocalDataSource.getInstance());
        mSubscription = new CompositeDisposable();
    }

    private void getGenreList() {
        Disposable subscribe = mGenreRepository.getListGenreLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        showProgressBar();
                    }
                })
                .subscribe(new Consumer<List<Genre>>() {
                    @Override
                    public void accept(List<Genre> genres) {
                        hideProgressBar();
                        onDataLoaded(genres);
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
    public GenreAdapter newAdapter() {
        return new GenreAdapter(mActivity, this, new ArrayList<>());
    }

    @Override
    public void onRecyclerViewItemClick(Genre dataItem) {
        mActivity.startActivity(GenreActivity.getInstance(mActivity, dataItem));
    }

    @Override
    public void onDataLoaded(List<Genre> data) {
        mAdapter.addItem(data);
    }

    @Override
    public void onStart() {
        getGenreList();
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    public void onGenrePlayButtonClick(Genre genre) {
        mActivity.startActivity(PlayMusicActivity.getInstance(mActivity));
    }
}
