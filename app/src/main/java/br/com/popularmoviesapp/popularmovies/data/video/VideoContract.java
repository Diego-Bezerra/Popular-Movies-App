package br.com.popularmoviesapp.popularmovies.data.video;

import android.net.Uri;

import br.com.popularmoviesapp.popularmovies.data.BaseContract;

public class VideoContract extends BaseContract {

    public static final String PATH_VIDEO = "video";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();

    public static final String TABLE_NAME = "video";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_MOVIE = "movie";
}
