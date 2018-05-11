package br.com.popularmoviesapp.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PopularMoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;

    public PopularMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_POSTER + " BLOB NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_POPULARITY +  " REAL NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_FAVORITE + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + MovieContract.VideoEntry.TABLE_NAME + " ("
                + MovieContract.VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.VideoEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_KEY + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_TYPE + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_MOVIE + " INTEGER NOT NULL, "
                + " (FOREIGN KEY ("+ MovieContract.VideoEntry.COLUMN_MOVIE +") REFERENCES ("+ MovieContract.MovieEntry._ID +")))";
        db.execSQL(SQL_CREATE_VIDEO_TABLE);

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " ("
                + MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, "
                + MovieContract.ReviewEntry.COLUMN_URL + " TEXT NOT NULL, "
                + MovieContract.ReviewEntry.COLUMN_MOVIE + " INTEGER NOT NULL, "
                + " (FOREIGN KEY ("+ MovieContract.ReviewEntry.COLUMN_MOVIE +") REFERENCES ("+ MovieContract.MovieEntry._ID +")))";
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
