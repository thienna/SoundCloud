package com.example.mike.mikemusic.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by ThienNA on 08/08/2018.
 */

public class Utils {
    public static String parseMilliSecondsToTimer(long time) {
        long min, sec, hour;
        time = time / 1000;
        min = TimeUnit.SECONDS.toMinutes(time);
        sec = time - min * 60;
        return String.format(Locale.ENGLISH, "%02d:%02d", min, sec);
    }
}
