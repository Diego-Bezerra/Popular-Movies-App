package br.com.popularmoviesapp.popularmovies.data.movie;

import android.content.UriMatcher;
import android.database.Cursor;

public class MovieProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIES);

        return matcher;
    }

    public static Cursor query() {

        Cursor cursor;
        String movieId;
        String[] selectionArguments;

        if (sUriMatcher.)
    }
}
