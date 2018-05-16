package br.com.popularmoviesapp.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.review.ReviewContract;
import br.com.popularmoviesapp.popularmovies.data.video.VideoContract;

public class PopularMoviesProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_ID = 101;
    public static final int CODE_VIDEOS = 102;
    public static final int CODE_REVIEWS = 103;
    public static final int CODE_VIDEOS_WITH_MOVIE_ID = 104;
    public static final int CODE_REVIEW_WITH_MOVIE_ID = 105;

    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private PopularMoviesDBHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIES_ID);
        matcher.addURI(authority, VideoContract.PATH_VIDEO + "/#", CODE_VIDEOS_WITH_MOVIE_ID);
        matcher.addURI(authority, ReviewContract.PATH_REVIEW + "/#", CODE_REVIEW_WITH_MOVIE_ID);

        return matcher;
    }

    public PopularMoviesProvider() {
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PopularMoviesDBHelper(getContext());
        mOpenHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String tableName;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
            case CODE_MOVIES_ID:
                tableName = MovieContract.TABLE_NAME;
                break;
            case CODE_VIDEOS_WITH_MOVIE_ID:
                tableName = VideoContract.TABLE_NAME;
                break;
            case CODE_REVIEW_WITH_MOVIE_ID:
                tableName = ReviewContract.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }

        Cursor cursor = mOpenHelper.getReadableDatabase()
                .query(tableName
                        , projection
                        , selection
                        , selectionArgs
                        , null
                        , null
                        , sortOrder);

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
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MovieContract.TABLE_NAME
                        , selection
                        , selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES_ID:
                return mOpenHelper.getWritableDatabase()
                        .update(MovieContract.TABLE_NAME, values, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
//        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        switch (sUriMatcher.match(uri)) {
//            case CODE_MOVIES:
//                db.beginTransaction();
//                int rowsInserted = 0;
//                try {
//                    for (ContentValues contentValues : values) {
//                        long id = db.insert(MovieContract.TABLE_NAME, null, contentValues);
//                        if (id != -1) {
//                            rowsInserted++;
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//
//                if (rowsInserted > 0 && getContext() != null) {
//                    getContext().getContentResolver().notifyChange(uri, null);
//                }
//
//                return rowsInserted;
//
//            default:
//                return super.bulkInsert(uri, values);
//        }
    }
}
