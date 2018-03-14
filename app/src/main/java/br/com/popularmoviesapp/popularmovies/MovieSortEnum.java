package br.com.popularmoviesapp.popularmovies;

import android.content.Context;

public enum MovieSortEnum {
    POPULAR(R.string.popular),
    TOP_RATED(R.string.top_rated);

    @SuppressWarnings("CanBeFinal")
    private int strValue;

    MovieSortEnum(int strValue) {
        this.strValue = strValue;
    }

    public String getStrValue(Context context) {
        return context.getString(strValue);
    }
}