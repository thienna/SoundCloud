package com.example.mike.mikemusic.screen.home;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.databinding.FragmentHomeBinding;
import com.example.mike.mikemusic.screen.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    private HomeViewModel mViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        // TODO: 8/7/2018 Add Bundle Later
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentHomeBinding fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_home, container, false);

        mViewModel = new HomeViewModel((AppCompatActivity) getContext());
        fragmentHomeBinding.setViewModel(mViewModel);
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModel.onStop();
    }
}
