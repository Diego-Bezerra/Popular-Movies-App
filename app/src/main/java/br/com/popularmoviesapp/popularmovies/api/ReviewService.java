package br.com.popularmoviesapp.popularmovies.api;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import br.com.popularmoviesapp.popularmovies.data.review.ReviewContract;
import br.com.popularmoviesapp.popularmovies.data.review.ReviewProviderUtil;
import br.com.popularmoviesapp.popularmovies.util.NetworkUtils;

public class ReviewService extends BaseService {

    private static final String REVIEWS_PATH = "reviews";
    private static final String AUTHOR_JSON = "author";
    private static final String CONTENT_JSON = "content";
    private static final String URL_JSON = "url";

    private static int syncingMovieId = 0;

    public static void syncReviewsData(int movieId, Context context) {

        try {

            if (syncingMovieId == movieId) return;
            if (!NetworkUtils.isNetworkAvailable(context)) {
                throw new IllegalStateException("No internet connection");
            }

            syncingMovieId = movieId;
            URL url = getMoviesApiURLWithId(REVIEWS_PATH, movieId);

            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray(RESULTS_JSON);

            if (results != null && results.length() > 0) {

                ContentValues[] contentValues = new ContentValues[results.length()];
                for (int i = 0; i < results.length(); i++) {
                    String json = results.getString(i);
                    contentValues[i] = getContentValuesFromJson(movieId, json);
                }

                ReviewProviderUtil.delete(movieId, context);
                ReviewProviderUtil.bulkInsert(contentValues, context);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            syncingMovieId = 0;
        }
    }

    private static ContentValues getContentValuesFromJson(int movieId, String json) throws JSONException {

        JSONObject jsonObj = new JSONObject(json);

        ContentValues values = new ContentValues();
        values.put(ReviewContract.COLUMN_AUTHOR, jsonObj.getString(AUTHOR_JSON));
        values.put(ReviewContract.COLUMN_CONTENT, jsonObj.getString(CONTENT_JSON));
        values.put(ReviewContract.COLUMN_URL, jsonObj.getDouble(URL_JSON));
        values.put(ReviewContract.COLUMN_MOVIE, movieId);

        return values;
    }
}
