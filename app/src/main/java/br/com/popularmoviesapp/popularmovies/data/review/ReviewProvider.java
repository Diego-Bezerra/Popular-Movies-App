package br.com.popularmoviesapp.popularmovies.data.review;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import br.com.popularmoviesapp.popularmovies.data.video.VideoContract;

public class ReviewProvider {

    public static Cursor getAllReviews(int movieId, Context context) {
        Uri uri = ReviewContract.CONTENT_URI.buildUpon().appendPath(movieId + "").build();
        return context.getContentResolver()
                .query(uri
                        , null
                        , ReviewContract.COLUMN_MOVIE + "=?"
                        , new String[]{movieId + ""}
                        , ReviewContract.COLUMN_AUTHOR + " ASC");
    }
}
