package br.com.popularmoviesapp.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class BaseContract implements BaseColumns {

    public static final String CONTENT_AUTHORITY = "br.com.popularmoviesapp.popularmovies.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
}
