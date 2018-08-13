package com.example.mike.mikemusic.screen.personal;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.databinding.FragmentPersonalBinding;
import com.example.mike.mikemusic.screen.BaseFragment;

public class PersonalFragment extends BaseFragment {

    private PersonalViewModel mViewModel;

    public PersonalFragment() {
        // Required empty public constructor
    }

    public static PersonalFragment newInstance() {
        PersonalFragment fragment = new PersonalFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentPersonalBinding fragmentPersonalBinding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_personal, container, false);

        mViewModel = new PersonalViewModel((AppCompatActivity) getContext());
        fragmentPersonalBinding.setViewModel(mViewModel);
        return fragmentPersonalBinding.getRoot();
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
