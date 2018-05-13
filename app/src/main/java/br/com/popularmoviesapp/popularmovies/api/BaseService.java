package br.com.popularmoviesapp.popularmovies.api;

import br.com.popularmoviesapp.popularmovies.BuildConfig;

public class BaseService {

    protected static final String BASE_URL = "http://api.themoviedb.org/3";
    protected static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    protected static final String IMAGE_SIZE_185_PATH = "w185";
    protected static final String API_KEY_QUERY = "api_key";
    protected static final String API_KEY = BuildConfig.API_KEY;
}
