package br.com.popularmoviesapp.popularmovies.data.video;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import br.com.popularmoviesapp.popularmovies.data.review.ReviewContract;

public class VideoProvider {

    public static Cursor getAllVideos(int movieId, Context context) {
        Uri uri = ReviewContract.CONTENT_URI.buildUpon().appendPath(movieId + "").build();
        return context.getContentResolver()
                .query(uri
                        , null
                        , VideoContract.COLUMN_MOVIE + "=?"
                        , new String[]{movieId + ""}
                        , VideoContract.COLUMN_NAME + " ASC");
    }

    public static int deleteAll(Context context) {
        return context.getContentResolver()
                .delete(VideoContract.CONTENT_URI
                        , null, null);
    }

    public static void bulkInsert(ContentValues[] contentValues, Context context) {
        context.getContentResolver()
                .bulkInsert(VideoContract.CONTENT_URI, contentValues);
    }

    public static void insertOrUpdate(int movieId, ContentValues contentValues, Context context) {
        Cursor cursor = context.getContentResolver()
                .query(VideoContract.CONTENT_URI
                        , new String[]{VideoContract._ID}
                        , VideoContract.COLUMN_MOVIE + "=?"
                        , new String[]{movieId + ""}
                        , null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            int videoId = cursor.getInt(cursor.getColumnIndex(VideoContract._ID));
            update(videoId, contentValues, context);
            cursor.close();
        } else {
            insert(contentValues, context);
        }
    }

    private static Uri insert(ContentValues contentValues, Context context) {
        return context.getContentResolver()
                .insert(VideoContract.CONTENT_URI
                        , contentValues);
    }

    private static int update(int videoId, ContentValues contentValues, Context context) {
        return context.getContentResolver()
                .update(VideoContract.CONTENT_URI
                        , contentValues
                        , VideoContract._ID + "=?"
                        , new String[]{videoId + ""});
    }
}
