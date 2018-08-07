package com.example.mike.mikemusic.screen.main;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.screen.home.HomeFragment;
import com.example.mike.mikemusic.screen.personal.PersonalFragment;
import com.example.mike.mikemusic.screen.playlist.PlaylistFragment;

/**
 * Created by ThienNA on 03/08/2018.
 */

public class MainViewModel extends BaseObservable implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    private Fragment mCurrentFragment;
    private Fragment mHomeFragment;
    private Fragment mPersonalFragment;
    private Fragment mPlaylistFragment;
    private FragmentManager mFragmentManager;
    private MainActivity mMainActivity;

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

    private void hideShowFragment(Fragment hide, Fragment show) {
        mFragmentManager.beginTransaction().hide(hide).show(show).commit();
    }

    private void createFragments() {
        // TODO: 8/7/2018 Create Search Fragment Later 
//        mSearchFragment = SearchFragment.newInstance();
        mHomeFragment = HomeFragment.newInstance();
        mPlaylistFragment = PlaylistFragment.newInstance();
        mPersonalFragment = PersonalFragment.newInstance();
        addHideFragment(mPlaylistFragment);
        addHideFragment(mPersonalFragment);
        mFragmentManager.beginTransaction().add(R.id.frame_container, mHomeFragment).commit();
        mCurrentFragment = mHomeFragment;
    }

    private void addHideFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().add(R.id.frame_container, fragment).hide(fragment).commit();
    }
}
