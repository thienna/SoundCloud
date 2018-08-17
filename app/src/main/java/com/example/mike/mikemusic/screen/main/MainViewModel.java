package com.example.mike.mikemusic.screen.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.BaseObservable;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.data.model.Track;
import com.example.mike.mikemusic.screen.BaseViewModel;
import com.example.mike.mikemusic.screen.home.HomeFragment;
import com.example.mike.mikemusic.screen.personal.PersonalFragment;
import com.example.mike.mikemusic.screen.playercontrol.PlayerControlFragment;
import com.example.mike.mikemusic.screen.playlist.PlaylistFragment;
import com.example.mike.mikemusic.screen.search.SearchFragment;
import com.example.mike.mikemusic.service.MusicService;
import com.example.mike.mikemusic.utils.Constants;
import com.example.mike.mikemusic.utils.music.PlaybackInfoListener;

/**
 * Created by ThienNA on 03/08/2018.
 */

public class MainViewModel extends BaseObservable implements BaseViewModel,
        BottomNavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener {
    private Fragment mCurrentFragment;
    private Fragment mHomeFragment;
    private Fragment mPersonalFragment;
    private Fragment mPlaylistFragment;
    private SearchFragment mSearchFragment;
    private Fragment mPlayerControlFragment;
    private FragmentManager mFragmentManager;
    private MainActivity mMainActivity;
    private Track mTrack;
    private PlaybackListener mListener;
    private String mQuery = "";

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
            mListener = new PlaybackListener();
            mMusicService.addPlaybackInfoListener(mListener);
            mTrack = mMusicService.getCurrentTrack();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBounded = false;
        }
    };

    public MainViewModel(Context context) {
        mMainActivity = (MainActivity) context;
        mFragmentManager = mMainActivity.getSupportFragmentManager();
        createFragments();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                hideShowFragment(mCurrentFragment, mHomeFragment);
                mCurrentFragment = mHomeFragment;
                return true;
            case R.id.navigation_playlist:
                hideShowFragment(mCurrentFragment, mPlaylistFragment);
                mCurrentFragment = mPlaylistFragment;
                return true;
            case R.id.navigation_personal:
                hideShowFragment(mCurrentFragment, mPersonalFragment);
                mCurrentFragment = mPersonalFragment;
                return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(mMainActivity, MusicService.class);
            mMainActivity.startService(mPlayIntent);
            mMainActivity.bindService(mPlayIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onStop() {
        if (mServiceBounded) {
            mMainActivity.unbindService(mServiceConnection);
            if (mListener != null) {
                mMusicService.removePlaybackInfoListener(mListener);
            }
            mServiceBounded = false;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mQuery.equals(query)) {
            return false;
        } else {
            mQuery = query;
            mMainActivity.setQuery(query, false);
            mSearchFragment.searchTracks(query, Constants.ApiSoundCloud.DEFAULT_PARAM_VALUE_OFFSET);
            mMainActivity.clearFocus();
            return true;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        mMainActivity.hideBottomNavigation();
        mFragmentManager.beginTransaction()
                .show(mSearchFragment)
                .hide(mCurrentFragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        mMainActivity.showBottomNavigation();
        mFragmentManager.popBackStack();
        return true;
    }

    private void hideShowFragment(Fragment hide, Fragment show) {
        mFragmentManager.beginTransaction().hide(hide).show(show).commit();
    }

    private void createFragments() {
        // TODO: 8/7/2018 Create Search Fragment Later 
//        mSearchFragment = SearchFragment.newInstance();
        mHomeFragment = HomeFragment.newInstance();
        mPlaylistFragment = PlaylistFragment.newInstance();
        mPersonalFragment = PersonalFragment.newInstance();
        mSearchFragment = SearchFragment.newInstance();
        mPlayerControlFragment = PlayerControlFragment.newInstance();
        addHideFragment(mPlaylistFragment);
        addHideFragment(mPersonalFragment);
        addHideFragment(mSearchFragment);
        mFragmentManager.beginTransaction().add(R.id.frame_container, mHomeFragment).add(R.id
                .fragment_player_control, mPlayerControlFragment).commit();
        mCurrentFragment = mHomeFragment;
    }

    private void addHideFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().add(R.id.frame_container, fragment).hide(fragment).commit();
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

    public interface SearchViewListener {
        void setQuery(String query, boolean submit);

        void clearFocus();

        void hideBottomNavigation();

        void showBottomNavigation();
    }
}
