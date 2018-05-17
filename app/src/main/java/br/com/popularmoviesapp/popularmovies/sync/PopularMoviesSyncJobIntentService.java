package br.com.popularmoviesapp.popularmovies.sync;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import br.com.popularmoviesapp.popularmovies.api.MovieService;
import br.com.popularmoviesapp.popularmovies.util.LogUtil;

public class PopularMoviesSyncJobIntentService extends JobIntentService {

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        LogUtil.logInfo("--------IntentService--------");
        MovieService.syncAllDataMovies(getApplicationContext());
    }
}
