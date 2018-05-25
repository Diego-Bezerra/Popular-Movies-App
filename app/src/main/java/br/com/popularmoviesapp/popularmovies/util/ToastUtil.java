package br.com.popularmoviesapp.popularmovies.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast;

    public static void showToast(int messageResource, Context context) {
        showToast(context.getString(messageResource), context);
    }

    public static void showToast(String message, Context context) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
