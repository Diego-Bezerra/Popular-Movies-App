package br.com.popularmoviesapp.popularmovies.sync;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import br.com.popularmoviesapp.popularmovies.api.MovieService;

public class PopularMoviesSyncJobIntentService extends JobIntentService {

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        MovieService.getPopularMovies(this.getBaseContext());
    }
}
