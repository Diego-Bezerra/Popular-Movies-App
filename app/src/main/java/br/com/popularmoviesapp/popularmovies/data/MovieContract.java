package br.com.popularmoviesapp.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "br.com.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("Content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_VIDEO = "video";
    public static final String PATH_REVIEW = "review";

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_FAVORITE = "favorite";
    }

    public static class VideoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();

        public static final String TABLE_NAME = "video";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_MOVIE = "movie";
    }

    public static class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String TABLE_NAME = "name";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_MOVIE = "movie";
    }
}
