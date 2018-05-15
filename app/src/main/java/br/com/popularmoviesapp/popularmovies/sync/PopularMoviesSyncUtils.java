package br.com.popularmoviesapp.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;

import br.com.popularmoviesapp.popularmovies.data.movie.MovieProvider;
import br.com.popularmoviesapp.popularmovies.gui.MovieSortEnum;

public class PopularMoviesSyncUtils {

    private static boolean isInitialized;
    
    synchronized public static void initialize(@NonNull final Context context) {

        if(isInitialized) return;
        isInitialized = true;
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = MovieProvider.getAllMoviesCursor(MovieSortEnum.POPULAR, context);
                if (cursor == null || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }
                if (cursor != null) cursor.close();
            }
        }).start();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, PopularMoviesSyncJobIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
