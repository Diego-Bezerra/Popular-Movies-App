package br.com.popularmoviesapp.popularmovies;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class MovieApiService {

    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_185_PATH = "w185";
    private static final String MOVIE_PATH = "movie";
    private static final String POPULAR_PATH = "popular";
    private static final String TOP_RATED_PATH = "top_rated";
    private static final String API_KEY_QUERY = "api_key";
    private static final String API_KEY = "[PUT_API_KEY_HERE]";
    private static final String RESULTS_PARAM = "results";

    private static URL getMoviesApiURL(String path) {

        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(path)
                .appendQueryParameter(API_KEY_QUERY, API_KEY).build();

        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<MovieResponse> getPopularMovies() {
        return getMovies(POPULAR_PATH);
    }

    public static ArrayList<MovieResponse> getTopRatedMovies() {
        return getMovies(TOP_RATED_PATH);
    }

    private static ArrayList<MovieResponse> getMovies(String path) {
        URL url = getMoviesApiURL(path);
        ArrayList<MovieResponse> movies = new ArrayList<>();
        try {

            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray(RESULTS_PARAM);

            for (int i = 0; i < results.length(); i++) {
                String json = results.getString(i);
                movies.add(new MovieResponse(json));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static String getImageThumbPath(String imageName) {
        return Uri.parse(BASE_IMAGE_URL)
                .buildUpon()
                .appendPath(IMAGE_SIZE_185_PATH)
                .appendEncodedPath(imageName)
                .build().toString();
    }
}
