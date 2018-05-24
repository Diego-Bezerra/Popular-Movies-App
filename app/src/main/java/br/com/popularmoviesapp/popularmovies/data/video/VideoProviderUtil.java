package br.com.popularmoviesapp.popularmovies.data.video;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import br.com.popularmoviesapp.popularmovies.api.VideoService;
import br.com.popularmoviesapp.popularmovies.util.NetworkUtils;

public class VideoProviderUtil {

    public static AsyncTaskLoader<Cursor> getVideosAsyncTaskLoaderByMovieId(final int movieId, final int movieApiId, final Context context) {
        return new AsyncTaskLoader<Cursor>(context) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                Cursor cursor = VideoProviderUtil.getAllVideosCursor(movieId, context);
                if (cursor == null || cursor.getCount() == 0) {
                    if (NetworkUtils.isNetworkAvailable(context)) {
                        VideoService.syncVideosData(movieId, movieApiId, context);
                        return VideoProviderUtil.getAllVideosCursor(movieId, context);
                    }
                }

                return cursor;
            }
        };
    }

    public static Cursor getAllVideosCursor(int movieId, Context context) {
        Uri uri = createUriWithMovieId(movieId);
        return context.getContentResolver()
                .query(uri
                        , null
                        , VideoContract.COLUMN_MOVIE + "=?"
                        , new String[]{movieId + ""}
                        , VideoContract.COLUMN_NAME + " ASC");
    }

    public static int deleteAll(Context context) {
        return context.getContentResolver().delete(VideoContract.CONTENT_URI, null, null);
    }

    public static int delete(int movieId, Context context) {
        Uri uri = createUriWithMovieId(movieId);
        return context.getContentResolver().delete(uri, null, null);
    }

    public static void bulkInsert(ContentValues[] contentValues, Context context) {
        context.getContentResolver()
                .bulkInsert(VideoContract.CONTENT_URI, contentValues);
    }

    private static Uri createUriWithMovieId(int movieId) {
        return VideoContract.CONTENT_URI.buildUpon().appendPath(movieId + "").build();
    }
}
