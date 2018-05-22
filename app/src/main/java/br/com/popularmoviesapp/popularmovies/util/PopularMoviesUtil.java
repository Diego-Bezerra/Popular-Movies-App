package br.com.popularmoviesapp.popularmovies.util;

import android.content.Context;
import android.os.Handler;

public class PopularMoviesUtil {

    public static void runOnMainThread(Runnable runnable, Context context) {
        new Handler(context.getMainLooper()).post(runnable);
    }
}
