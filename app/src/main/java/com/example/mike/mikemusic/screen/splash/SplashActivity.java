package com.example.mike.mikemusic.screen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.mike.mikemusic.screen.BaseActivity;
import com.example.mike.mikemusic.screen.main.MainActivity;

/**
 * Created by ThienNA on 02/08/2018.
 */

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_TIMEOUT = 1500;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIMEOUT);
    }
}
