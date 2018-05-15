package br.com.popularmoviesapp.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.review.ReviewContract;
import br.com.popularmoviesapp.popularmovies.data.video.VideoContract;

public class PopularMoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;

    public PopularMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.TABLE_NAME + " ("
                + MovieContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.COLUMN_SYNOPSIS + " TEXT NOT NULL, "
                + MovieContract.COLUMN_POSTER + " BLOB, "
                + MovieContract.COLUMN_POSTER_URL + " TEXT NOT NULL, "
                + MovieContract.COLUMN_POPULARITY +  " REAL NOT NULL, "
                + MovieContract.COLUMN_AVERAGE +  " REAL NOT NULL, "
                + MovieContract.COLUMN_FAVORITE + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + VideoContract.TABLE_NAME + " ("
                + VideoContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VideoContract.COLUMN_NAME + " TEXT NOT NULL, "
                + VideoContract.COLUMN_KEY + " TEXT NOT NULL, "
                + VideoContract.COLUMN_TYPE + " TEXT NOT NULL, "
                + VideoContract.COLUMN_MOVIE + " INTEGER NOT NULL, "
                + " FOREIGN KEY ("+ VideoContract.COLUMN_MOVIE +") REFERENCES "+VideoContract.TABLE_NAME+"("+ VideoContract._ID +"));";
        db.execSQL(SQL_CREATE_VIDEO_TABLE);

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewContract.TABLE_NAME + " ("
                + ReviewContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReviewContract.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + ReviewContract.COLUMN_CONTENT + " TEXT NOT NULL, "
                + ReviewContract.COLUMN_URL + " TEXT NOT NULL, "
                + ReviewContract.COLUMN_MOVIE + " INTEGER NOT NULL, "
                + " FOREIGN KEY ("+ ReviewContract.COLUMN_MOVIE +") REFERENCES "+ ReviewContract.TABLE_NAME +"("+ ReviewContract._ID +"));";
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_NAME);
        onCreate(db);
    }
}
