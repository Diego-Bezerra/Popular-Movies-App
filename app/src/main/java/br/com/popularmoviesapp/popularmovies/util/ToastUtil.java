package br.com.popularmoviesapp.popularmovies.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast;

    public static void showToast(int messageResource, Context context) {
        showToast(context.getString(messageResource), context);
    }

    public static void showToast(final String message, final Context context) {
        PopularMoviesUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (toast != null) toast.cancel();
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                toast.show();
            }
        }, context);
    }
}
