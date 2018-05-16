package br.com.popularmoviesapp.popularmovies.api;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProvider;
import br.com.popularmoviesapp.popularmovies.util.NetworkUtils;

public class MovieService extends BaseService {

    private static final String POPULAR_PATH = "popular";
    private static final String TOP_RATED_PATH = "top_rated";
    //json keys
    private static final String TITLE_JSON = "title";
    private static final String VOTE_AVERAGE_JSON = "vote_average";
    private static final String POPULARITY_JSON = "popularity";
    private static final String POSTER_PATH_JSON = "poster_path";
    private static final String OVERVIEW_JSON = "overview";
    private static final String RELEASE_DATE_JSON = "release_date";

    public static void syncMoviesData(Context context) {
        getMovies(context, POPULAR_PATH);
    }

    private static void getMovies(Context context, String path) {

        try {

            if (!NetworkUtils.isNetworkAvailable(context)) {
                throw new IllegalStateException("No internet connection");
            }

            URL url = getMoviesApiURL(path);

            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray(RESULTS_JSON);

            if (results != null && results.length() > 0) {

                ContentValues[] contentValues = new ContentValues[results.length()];
                for (int i = 0; i < results.length(); i++) {
                    String json = results.getString(i);
                    contentValues[i] = getContentValuesFromJson(json);
                }

                MovieProvider.deleteAll(context);
                MovieProvider.bulkInsert(contentValues, context);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static ContentValues getContentValuesFromJson(String json) throws JSONException {

        JSONObject jsonObj = new JSONObject(json);

        ContentValues values = new ContentValues();
        values.put(MovieContract.COLUMN_TITLE, jsonObj.getString(TITLE_JSON));
        values.put(MovieContract.COLUMN_SYNOPSIS, jsonObj.getString(OVERVIEW_JSON));
        values.put(MovieContract.COLUMN_AVERAGE, jsonObj.getDouble(VOTE_AVERAGE_JSON));
        values.put(MovieContract.COLUMN_FAVORITE, false);
        values.put(MovieContract.COLUMN_POSTER_URL, jsonObj.getString(POSTER_PATH_JSON).replace("/", ""));
        values.put(MovieContract.COLUMN_POPULARITY, jsonObj.getDouble(POPULARITY_JSON));
        values.put(MovieContract.COLUMN_RELEASE_DATE, jsonObj.getString(RELEASE_DATE_JSON));

        return values;
    }

    public static String getImageThumbPath(String imageName) {
        return Uri.parse(BASE_IMAGE_URL)
                .buildUpon()
                .appendPath(IMAGE_SIZE_185_PATH)
                .appendEncodedPath(imageName)
                .build().toString();
    }
}
