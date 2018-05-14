package br.com.popularmoviesapp.popularmovies.data.video;

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
                        , VideoContract.COLUMN_NAME+ " ASC");
    }
}
