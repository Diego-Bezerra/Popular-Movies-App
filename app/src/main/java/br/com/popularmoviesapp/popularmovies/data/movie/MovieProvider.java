package br.com.popularmoviesapp.popularmovies.data.movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import br.com.popularmoviesapp.popularmovies.gui.MovieSortEnum;

public class MovieProvider {

    public static Loader<Cursor> getAllMoviesCursorLoader(MovieSortEnum sortEnum, Context context) {
        String sortColumn = getSortColumn(sortEnum);
        String selection = null;
        String[] selectionArgs = null;
        if (sortEnum == MovieSortEnum.FAVORITE) {
            selection = MovieContract.COLUMN_FAVORITE + "=?";
            selectionArgs = new String[]{"1"};
        }

        return new CursorLoader(context
                , MovieContract.CONTENT_URI
                , null
                , selection
                , selectionArgs
                , sortColumn + " DESC");
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

    public static void bulkInsert(ContentValues[] contentValues, Context context) {
        context.getContentResolver()
                .bulkInsert(MovieContract.CONTENT_URI, contentValues);
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
                , MovieContract._ID + "=?"
                , new String[]{movieId + ""}
                , MovieContract.COLUMN_POPULARITY + " DESC");
    }

    public static int updateMoviePoster(int movieId, byte[] img, Context context) {
        Uri uri = MovieContract.CONTENT_URI.buildUpon().appendPath(movieId + "").build();
        ContentValues values = new ContentValues();
        values.put(MovieContract.COLUMN_POSTER, img);
        return context.getContentResolver()
                .update(uri
                        , values
                        , MovieContract._ID + "=?"
                        , new String[]{movieId + ""});
    }
}
