package br.com.popularmoviesapp.popularmovies.sync;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import java.net.MalformedURLException;

import br.com.popularmoviesapp.popularmovies.api.MovieService;

public class PopularMoviesSyncJobIntentService extends JobIntentService {

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        try {
            MovieService.getPopularMovies(this.getBaseContext());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
