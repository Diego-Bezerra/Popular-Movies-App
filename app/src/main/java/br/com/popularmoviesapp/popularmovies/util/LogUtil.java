package br.com.popularmoviesapp.popularmovies.util;

import android.util.Log;

public class LogUtil {

    static final String LOG_TAG = "popular_movie_app_tag";

    public static void logInfo(String info) {
        Log.i(LOG_TAG, info);
    }
}
