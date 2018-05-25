package br.com.popularmoviesapp.popularmovies.api;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

import br.com.popularmoviesapp.popularmovies.BuildConfig;

public class BaseService {

    private static final String MOVIE_PATH = "movie";
    private static final String BASE_URL = "http://api.themoviedb.org/3";
    protected static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE_185_PATH = "w185";
    public static final String IMAGE_SIZE_780_PATH = "w780";
    private static final String API_KEY_QUERY = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;
    protected static final String RESULTS_JSON = "results";
    protected static final String ID_JSON = "id";

    protected static URL getMoviesApiURL(String path) throws MalformedURLException {

        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(path)
                .appendQueryParameter(API_KEY_QUERY, API_KEY).build();

        return new URL(uri.toString());
    }

    protected static URL getMoviesApiURLWithId(String path, int id) throws MalformedURLException {

        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(id + "")
                .appendPath(path)
                .appendQueryParameter(API_KEY_QUERY, API_KEY).build();

        return new URL(uri.toString());
    }
}
