package br.com.popularmoviesapp.popularmovies.data.movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import br.com.popularmoviesapp.popularmovies.api.MovieService;
import br.com.popularmoviesapp.popularmovies.gui.MovieSortEnum;

public class MovieProviderUtil {

    public static AsyncTaskLoader<Cursor> getAllMoviesAsyncTaskLoader(@NonNull final MovieSortEnum sortEnum, @NonNull final Context context) {

        return new AsyncTaskLoader<Cursor>(context) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                Cursor cursor = getAllMoviesCursor(sortEnum, context);
                if (cursor == null || cursor.getCount() == 0) {
                    MovieService.syncMoviesData(context);
                    return getAllMoviesCursor(sortEnum, context);
                }

                return cursor;
            }
        };
    }

    public static Cursor getAllMoviesCursor(MovieSortEnum sortEnum, Context context) {
        String sortColumn = getSortColumn(sortEnum);
        String selection = null;
        String[] selectionArgs = null;
        if (sortEnum == MovieSortEnum.FAVORITE) {
            selection = MovieContract.COLUMN_FAVORITE + "=?";
            selectionArgs = new String[]{"1"};
        }

        return context.getContentResolver()
                .query(MovieContract.CONTENT_URI
                        , null
                        , selection
                        , selectionArgs
                        , sortColumn + " DESC");
    }

    public static int deleteAll(Context context) {
        return context.getContentResolver()
                .delete(MovieContract.CONTENT_URI
                        , null
                        , null);
    }

    public static int bulkInsert(ContentValues[] contentValues, Context context) {
        return bulkInsert(contentValues, false, context);
    }

    public static int bulkInsert(ContentValues[] contentValues, boolean notify, Context context) {
        int rowsInserted = context.getContentResolver()
                .bulkInsert(MovieContract.CONTENT_URI, contentValues);

        if (rowsInserted > 0 && notify) {
            context.getContentResolver().notifyChange(MovieContract.CONTENT_URI, null);
        }

        return rowsInserted;
    }

    private static String getSortColumn(MovieSortEnum sortEnum) {
        String sortColumn = MovieContract.COLUMN_POPULARITY;
        if (sortEnum == MovieSortEnum.TOP_RATED) {
            sortColumn = MovieContract.COLUMN_AVERAGE;
        }

        return sortColumn;
    }

    public static Loader<Cursor> getMovieById(int movieId, Context context) {
        Uri uri = MovieContract.CONTENT_URI.buildUpon().appendPath(movieId + "").build();
        return new CursorLoader(context
                , uri
                , null
                , null
                , null
                , MovieContract.COLUMN_POPULARITY + " DESC");
    }

    public static int updateMoviePoster(int movieId, String column, byte[] img, Context context) {
        Uri uri = MovieContract.CONTENT_URI.buildUpon().appendPath(movieId + "").build();
        ContentValues values = new ContentValues();
        values.put(column, img);
        return context.getContentResolver()
                .update(uri
                        , values
                        , null
                        , null);
    }

    public static int updateFavorite(boolean isFavorite, int movieId, Context context) {
        Uri uri = MovieContract.CONTENT_URI.buildUpon().appendPath(movieId + "").build();
        ContentValues values = new ContentValues();
        values.put(MovieContract.COLUMN_FAVORITE, isFavorite);
        int updatedRows = context.getContentResolver()
                .update(uri
                        , values
                        , null
                        , null);

        if (updatedRows > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;
    }

    public static void deleteAllData(Context context) {
        Uri uri = MovieContract.CONTENT_URI.buildUpon()
                .appendPath(MovieContract.PATH_MOVIES_DELETE_ALL_DATA).build();
        context.getContentResolver()
                .delete(uri, null, null);
    }
}
