package br.com.popularmoviesapp.popularmovies.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import br.com.popularmoviesapp.popularmovies.R;

public class NetworkUtils {

    public static String getResponseFromHttpUrl(URL url, final Context context) throws IOException {

        if (!NetworkUtils.isNetworkAvailable(context)) {
            if (context instanceof Activity) {
                PopularMoviesUtil.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                    }
                }, context);
            }
            return null;
        }

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
