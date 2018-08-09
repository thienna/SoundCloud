package com.example.mike.mikemusic.utils;

import com.example.mike.mikemusic.BuildConfig;

/**
 * Created by ThienNA on 02/08/2018.
 */

public final class Constants {

    public static final String BREAK_LINE = "\n";
    public static final int DEFAULT_CACHE_SIZE = 5 * 1024 * 1024;

    private Constants() {

    }

    public static final class ApiSoundCloud {
        public static final String BASE_URL = "https://api-v2.soundcloud.com";

        public static final String PARAM_KEY_GENRE = "genre";
        public static final String PARAM_KEY_OFFSET = "offset";
        public static final String PARAM_KEY_KIND = "kind";
        public static final String PARAM_KEY_LIMIT = "limit";
        public static final String PARAM_KEY_CLIENT_ID = "client_id";

        public static final String DEFAULT_PARAM_VALUE_GENRE = "soundcloud%3Agenres%3A";
        public static final int DEFAULT_PARAM_VALUE_OFFSET = 0;
        public static final String DEFAULT_PARAM_VALUE_KIND = "top";
        public static final String DEFAULT_PARAM_VALUE_CLIENT_ID = BuildConfig.API_KEY;
        public static final int DEFAULT_PARAM_VALUE_LIMIT = 10;

        private ApiSoundCloud() {
        }
    }
}
