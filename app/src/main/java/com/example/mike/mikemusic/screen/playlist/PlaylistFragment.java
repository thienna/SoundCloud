package com.example.mike.mikemusic.screen.playlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.screen.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends BaseFragment {


    public PlaylistFragment() {
        // Required empty public constructor
    }

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

}
