package br.com.popularmoviesapp.popularmovies.gui;

import android.content.Context;

import br.com.popularmoviesapp.popularmovies.R;

public enum MovieSortEnum {

    POPULAR(R.string.popular),
    TOP_RATED(R.string.top_rated),
    FAVORITE(R.string.favorites);

    @SuppressWarnings("CanBeFinal")
    private int strValue;

    MovieSortEnum(int strValue) {
        this.strValue = strValue;
    }

    public String getStrValue(Context context) {
        return context.getString(strValue);
    }
}