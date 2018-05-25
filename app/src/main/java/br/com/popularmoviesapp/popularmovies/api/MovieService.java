package br.com.popularmoviesapp.popularmovies.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProviderUtil;
import br.com.popularmoviesapp.popularmovies.gui.MovieSortEnum;
import br.com.popularmoviesapp.popularmovies.util.NetworkUtils;

public class MovieService extends BaseService {

    private static final String POPULAR_PATH = "popular";
    private static final String TOP_RATED_PATH = "top_rated";
    //json keys
    private static final String TITLE_JSON = "title";
    private static final String API_ID_JSON = "id";
    private static final String VOTE_AVERAGE_JSON = "vote_average";
    private static final String POPULARITY_JSON = "popularity";
    private static final String POSTER_PATH_JSON = "poster_path";
    private static final String OVERVIEW_JSON = "overview";
    private static final String RELEASE_DATE_JSON = "release_date";

    public static void syncAllDataMovies(Context context) {

        MovieService.syncMoviesData(context);
        Cursor cursor = MovieProviderUtil.getAllMoviesCursor(MovieSortEnum.POPULAR, context);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int movieId = cursor.getInt(cursor.getColumnIndex(MovieContract._ID));
                    VideoService.syncVideosData(movieId,  context);
                    ReviewService.syncReviewsData(movieId, context);
                }
            }
            cursor.close();
        }
    }

    public static void syncMoviesData(Context context) {
        getMovies(context, POPULAR_PATH);
    }

    private static void getMovies(Context context, String path) {

        try {

            URL url = getMoviesApiURL(path);

            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url, context);
            if (jsonResponse == null) return;
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray(RESULTS_JSON);

            if (results != null && results.length() > 0) {

                ContentValues[] contentValues = new ContentValues[results.length()];
                for (int i = 0; i < results.length(); i++) {
                    String json = results.getString(i);
                    contentValues[i] = getContentValuesFromJson(json);
                }

                MovieProviderUtil.bulkInsert(contentValues, context);
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
        values.put(MovieContract._ID, jsonObj.getString(ID_JSON));
        values.put(MovieContract.COLUMN_TITLE, jsonObj.getString(TITLE_JSON));
        values.put(MovieContract.COLUMN_SYNOPSIS, jsonObj.getString(OVERVIEW_JSON));
        values.put(MovieContract.COLUMN_AVERAGE, jsonObj.getDouble(VOTE_AVERAGE_JSON));
        values.put(MovieContract.COLUMN_FAVORITE, false);
        values.put(MovieContract.COLUMN_POSTER_URL, jsonObj.getString(POSTER_PATH_JSON).replace("/", ""));
        values.put(MovieContract.COLUMN_POPULARITY, jsonObj.getDouble(POPULARITY_JSON));
        values.put(MovieContract.COLUMN_RELEASE_DATE, jsonObj.getString(RELEASE_DATE_JSON));

        return values;
    }

    public static void downloadPoster(String urlPath, String column, int movieId, String sizePath, Context context) {

        InputStream in = null;
        try {
            URL url = new URL(getImageThumbPath(urlPath, sizePath));
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.connect();
            in = httpConn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Bitmap bitmap = BitmapFactory.decodeStream(in);
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            MovieProviderUtil.updateMoviePoster(movieId, column, byteArray, context);
        }
    }

    public static String getImageThumbPath(String imageName, String sizePath) {
        return Uri.parse(BASE_IMAGE_URL)
                .buildUpon()
                .appendPath(sizePath)
                .appendEncodedPath(imageName)
                .build().toString();
    }
}
