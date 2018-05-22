package br.com.popularmoviesapp.popularmovies.data.review;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import br.com.popularmoviesapp.popularmovies.api.ReviewService;

public class ReviewProviderUtil {

    public static AsyncTaskLoader<Cursor> getReviewsAsyncTaskLoaderByMovieId(final int movieId, final int movieApiId, final Context context) {
        return new AsyncTaskLoader<Cursor>(context) {
            @Nullable
            @Override
            public Cursor loadInBackground() {
                Cursor cursor = ReviewProviderUtil.getAllReviews(movieId, context);
                if (cursor == null || cursor.getCount() == 0) {
                    ReviewService.syncReviewsData(movieId, movieApiId, context);
                    return ReviewProviderUtil.getAllReviews(movieId, context);
                }

                return cursor;
            }
        };
    }

    //if (NetworkUtils.isNetworkAvailable(this)) {

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
