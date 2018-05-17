package br.com.popularmoviesapp.popularmovies.data.video;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class VideoProviderUtil {

    public static Cursor getAllVideos(int movieId, Context context) {
        Uri uri = createUriWithMovieId(movieId);
        return context.getContentResolver()
                .query(uri
                        , null
                        ,null
                        , null
                        , VideoContract.COLUMN_NAME + " ASC");
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
