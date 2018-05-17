package br.com.popularmoviesapp.popularmovies.data.review;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ReviewProviderUtil {

    public static Cursor getAllReviews(int reviewId, Context context) {
        Uri uri = createUriWithReviewId(reviewId);
        return context.getContentResolver()
                .query(uri
                        , null
                        ,null
                        , null
                        , ReviewContract.COLUMN_AUTHOR + " ASC");
    }

    public static int delete(int reviewId, Context context) {
        Uri uri = createUriWithReviewId(reviewId);
        return context.getContentResolver().delete(uri, null, null);
    }

    public static void bulkInsert(ContentValues[] contentValues, Context context) {
        context.getContentResolver()
                .bulkInsert(ReviewContract.CONTENT_URI, contentValues);
    }

    private static Uri createUriWithReviewId(int movieId) {
        return ReviewContract.CONTENT_URI.buildUpon().appendPath(movieId + "").build();
    }
}
