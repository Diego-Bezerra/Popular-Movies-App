package br.com.popularmoviesapp.popularmovies.data.review;

import android.net.Uri;

import br.com.popularmoviesapp.popularmovies.data.BaseContract;

public class ReviewContract extends BaseContract {

    public static final String PATH_REVIEW = "reviews";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

    public static final String TABLE_NAME = "review";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_MOVIE = "movie";
}
