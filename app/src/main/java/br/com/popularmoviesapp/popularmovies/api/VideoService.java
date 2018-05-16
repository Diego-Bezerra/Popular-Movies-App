package br.com.popularmoviesapp.popularmovies.api;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import br.com.popularmoviesapp.popularmovies.data.video.VideoContract;
import br.com.popularmoviesapp.popularmovies.data.video.VideoProvider;
import br.com.popularmoviesapp.popularmovies.util.NetworkUtils;

public class VideoService extends BaseService {

    private static final String VIDEOS_PATH = "%d/videos";
    private static final String KEY_JSON = "key";
    private static final String NAME_JSON = "name";
    private static final String TYPE_JSON = "type";

    private static void syncVideosData(int movieId, Context context, String path) {

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
                    contentValues[i] = getContentValuesFromJson(movieId, json);
                }

                VideoProvider.deleteAll(context);
                VideoProvider.bulkInsert(contentValues, context);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static ContentValues getContentValuesFromJson(int movieId, String json) throws JSONException {

        JSONObject jsonObj = new JSONObject(json);

        ContentValues values = new ContentValues();
        values.put(VideoContract.COLUMN_KEY, jsonObj.getString(KEY_JSON));
        values.put(VideoContract.COLUMN_NAME, jsonObj.getString(NAME_JSON));
        values.put(VideoContract.COLUMN_TYPE, jsonObj.getDouble(TYPE_JSON));
        values.put(VideoContract.COLUMN_MOVIE, movieId);

        return values;
    }
}
