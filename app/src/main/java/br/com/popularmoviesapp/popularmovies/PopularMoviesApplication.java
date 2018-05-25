package br.com.popularmoviesapp.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

public class PopularMoviesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new com.squareup.picasso.OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);
    }
}
