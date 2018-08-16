package com.example.mike.mikemusic.screen.playercontrol;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.databinding.FragmentPlayerControlBinding;

/**
 * Created by ThienNA on 17/08/2018.
 */

public class PlayerControlFragment extends Fragment {
    private PlayerControlViewModel mViewModel;

    public PlayerControlFragment() {
        // Required empty public constructor
    }

    public static PlayerControlFragment newInstance() {
        // TODO: 8/7/2018 Add Bundle Later
        return new PlayerControlFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentPlayerControlBinding fragmentPlayerControlBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_player_control, container, false);

        mViewModel = new PlayerControlViewModel((AppCompatActivity) getContext(), fragmentPlayerControlBinding);
        fragmentPlayerControlBinding.setViewModel(mViewModel);
        return fragmentPlayerControlBinding.getRoot();
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
