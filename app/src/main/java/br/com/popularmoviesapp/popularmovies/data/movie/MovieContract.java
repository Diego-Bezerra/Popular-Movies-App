package br.com.popularmoviesapp.popularmovies.data.movie;

import android.net.Uri;

import br.com.popularmoviesapp.popularmovies.data.BaseContract;

public class MovieContract extends BaseContract {

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_MOVIE_POSTER = PATH_MOVIE + "/poster";
    public static final String PATH_MOVIE_FAVORITE = PATH_MOVIE + "/favorite";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

    public static final String TABLE_NAME = "movie";
    public static final String COLUMN_API_ID = "apiId";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RELEASE_DATE = "releaseDate";
    public static final String COLUMN_SYNOPSIS = "synopsis";
    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_POSTER_URL = "posteUrl";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_AVERAGE = "average";
    public static final String COLUMN_FAVORITE = "favorite";
    
}
