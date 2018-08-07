package com.example.mike.mikemusic.data.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.mike.mikemusic.data.model.GenreType.ALL_AUDIO;
import static com.example.mike.mikemusic.data.model.GenreType.ALL_MUSIC;
import static com.example.mike.mikemusic.data.model.GenreType.ALTERNATIVE_ROCK;
import static com.example.mike.mikemusic.data.model.GenreType.AMBIENT;
import static com.example.mike.mikemusic.data.model.GenreType.CLASSICAL;
import static com.example.mike.mikemusic.data.model.GenreType.COUNTRY;


/**
 * Created by ThienNA on 05/08/2018.
 */

@StringDef({ALL_MUSIC, ALL_AUDIO, CLASSICAL, AMBIENT, COUNTRY, ALTERNATIVE_ROCK})
@Retention(RetentionPolicy.SOURCE)
public @interface GenreType {
    String ALL_MUSIC = "all-music";
    String ALL_AUDIO = "all-audio";
    String CLASSICAL = "classical";
    String AMBIENT = "ambient";
    String COUNTRY = "country";
    String ALTERNATIVE_ROCK = "alternativerock";
}
