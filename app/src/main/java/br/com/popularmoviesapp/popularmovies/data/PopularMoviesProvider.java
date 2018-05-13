package br.com.popularmoviesapp.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;

public class PopularMoviesProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_ID = 101;
    public static final int CODE_VIDEOS = 102;
    public static final int CODE_REVIEWS = 103;
    public static final int CODE_VIDEOS_WITH_MOVIE_ID = 104;
    public static final int CODE_REVIEW_WITH_MOVIE_ID = 105;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PopularMoviesDBHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/#", CODE_VIDEOS_WITH_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", CODE_REVIEW_WITH_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PopularMoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        String movieId;
        String[] selectionArguments;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
                cursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MovieEntry.TABLE_NAME
                                , projection
                                , selection
                                , selectionArgs
                                , null
                                , null
                                , sortOrder);
                break;
            case CODE_VIDEOS_WITH_MOVIE_ID:
                movieId = uri.getLastPathSegment();
                selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.VideoEntry.TABLE_NAME
                                , projection
                                , MovieContract.VideoEntry.COLUMN_MOVIE + " = ? "
                                , selectionArguments
                                , null
                                , null
                                , sortOrder);
                break;
            case CODE_REVIEW_WITH_MOVIE_ID:
                movieId = uri.getLastPathSegment();
                selectionArguments = new String[]{movieId};
                cursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.ReviewEntry.TABLE_NAME
                                , projection
                                , MovieContract.ReviewEntry.COLUMN_MOVIE + " = ? "
                                , selectionArguments
                                , null
                                , null
                                , sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }

        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES_ID:
                String movieId = uri.getLastPathSegment();
                mOpenHelper.getWritableDatabase()
                        .update(MovieContract.MovieEntry.TABLE_NAME
                                , values
                                , MovieContract.MovieEntry._ID + "=?"
                                , new String[] {movieId});
                break;
        }

        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }

    public static void updateMoviePoster() {

    }
}
