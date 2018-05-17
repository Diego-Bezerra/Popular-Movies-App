package br.com.popularmoviesapp.popularmovies.api;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import br.com.popularmoviesapp.popularmovies.data.video.VideoContract;
import br.com.popularmoviesapp.popularmovies.data.video.VideoProviderUtil;
import br.com.popularmoviesapp.popularmovies.util.LogUtil;
import br.com.popularmoviesapp.popularmovies.util.NetworkUtils;

public class VideoService extends BaseService {

    private static final String VIDEOS_PATH = "videos";
    private static final String KEY_JSON = "key";
    private static final String NAME_JSON = "name";
    private static final String TYPE_JSON = "type";
    private static int syncingMovieId = 0;


    private static int getSyncingMovieId() {
        return syncingMovieId;
    }

    public static void syncVideosData(int movieId, Context context) {

        try {

            LogUtil.logInfo("VIDEOS: Id = " + movieId);
            LogUtil.logInfo("VIDEOS: syncingMovieId == movieId --> " + (syncingMovieId == movieId));
            if (syncingMovieId == movieId) return;
            if (!NetworkUtils.isNetworkAvailable(context)) {
                throw new IllegalStateException("No internet connection");
            }

            syncingMovieId = movieId;
            URL url = getMoviesApiURLWithId(VIDEOS_PATH, movieId);


            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray(RESULTS_JSON);

            if (results != null && results.length() > 0) {

                ContentValues[] contentValues = new ContentValues[results.length()];
                for (int i = 0; i < results.length(); i++) {
                    String json = results.getString(i);
                    contentValues[i] = getContentValuesFromJson(movieId, json);
                }

                VideoProviderUtil.delete(movieId, context);
                LogUtil.logInfo("VIDEOS: delete all Id = " + movieId);
                VideoProviderUtil.bulkInsert(contentValues, context);
                LogUtil.logInfo("VIDEOS: bulkInsert Id = " + movieId + " contentValues = " + contentValues.length);
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
        values.put(VideoContract.COLUMN_KEY, jsonObj.getString(KEY_JSON));
        values.put(VideoContract.COLUMN_NAME, jsonObj.getString(NAME_JSON));
        values.put(VideoContract.COLUMN_TYPE, jsonObj.getString(TYPE_JSON));
        values.put(VideoContract.COLUMN_MOVIE, movieId);

        return values;
    }
}
