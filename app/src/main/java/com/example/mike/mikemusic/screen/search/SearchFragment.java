package com.example.mike.mikemusic.screen.search;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.databinding.FragmentSearchBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private SearchViewModel mViewModel;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchBinding fragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_search, container, false);

        mViewModel = new SearchViewModel((AppCompatActivity) getContext());
        fragmentSearchBinding.setViewModel(mViewModel);
        return fragmentSearchBinding.getRoot();
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

    public void searchTracks(String query, int offset) {
        mViewModel.searchTracks(query, offset);
    }
}
