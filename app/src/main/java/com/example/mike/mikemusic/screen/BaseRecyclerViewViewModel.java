package com.example.mike.mikemusic.screen;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.mike.mikemusic.BR;

import java.util.List;

/**
 * Created by ThienNA on 05/08/2018.
 */

public abstract class BaseRecyclerViewViewModel<V, T extends BaseRecyclerViewAdapter> extends
        BaseObservable implements BaseViewModel {

    protected T mAdapter;
    protected boolean mProgressBarVisibility;
    protected int mEmptyViewVisible;
    protected AppCompatActivity mActivity;

    private BaseRecyclerViewViewModel() {
    }

    public BaseRecyclerViewViewModel(AppCompatActivity activity) {
        mActivity = activity;
        mEmptyViewVisible = View.GONE;
        mAdapter = newAdapter();
    }

    @Bindable
    public int getEmptyViewVisible() {
        return mEmptyViewVisible;
    }

    @Bindable
    public boolean isProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public void setEmptyViewVisible(int emptyViewVisible) {
        mEmptyViewVisible = emptyViewVisible;
        notifyPropertyChanged(BR.emptyViewVisible);
    }

    public abstract T newAdapter();

    public abstract void onRecyclerViewItemClick(V dataItem);

    public abstract void onDataLoaded(List<V> data);

    public abstract void onStart();

    public abstract void onStop();

    public void setProgressBarVisibility(boolean progressBarVisibility) {
        mProgressBarVisibility = progressBarVisibility;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    public void showProgressBar() {
        setProgressBarVisibility(true);
    }

    public void hideProgressBar() {
        setProgressBarVisibility(false);
    }

    public void onDataLoadFailure(String message) {
        hideProgressBar();
        setEmptyViewVisible(View.VISIBLE);
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    public BaseRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }
}
