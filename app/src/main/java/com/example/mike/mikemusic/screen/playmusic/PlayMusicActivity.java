package com.example.mike.mikemusic.screen.playmusic;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mike.mikemusic.R;
import com.example.mike.mikemusic.databinding.ActivityPlayMusicBinding;

public class PlayMusicActivity extends AppCompatActivity {

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, PlayMusicActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPlayMusicBinding activityPlayMusicBinding = DataBindingUtil.setContentView(this, R
                .layout
                .activity_play_music);
        PlayMusicViewModel playMusicViewModel = new PlayMusicViewModel(this);
        initView();
        activityPlayMusicBinding.setViewModel(playMusicViewModel);
    }

    private void initView() {

    }
}
