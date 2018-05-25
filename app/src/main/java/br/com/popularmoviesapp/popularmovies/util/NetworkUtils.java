package br.com.popularmoviesapp.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static NetworkListener networkListener;

    public interface NetworkListener {
        void noInternetConnection();
    }

    public static void setNetworkListener(NetworkListener networkListener) {
        NetworkUtils.networkListener = networkListener;
    }

    public static String getResponseFromHttpUrl(URL url, final Context context) throws IOException {

        if (!NetworkUtils.isNetworkAvailable(context)) {
            if (networkListener != null) {
                networkListener.noInternetConnection();
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
