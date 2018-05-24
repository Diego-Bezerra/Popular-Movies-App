package br.com.popularmoviesapp.popularmovies.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.lang.ref.WeakReference;

import br.com.popularmoviesapp.popularmovies.api.MovieService;

public class PopularMoviesJobService extends JobService {

    private MyJobAsyncTask mAsync;

    @Override
    public boolean onStartJob(JobParameters job) {
        mAsync = new MyJobAsyncTask(getApplicationContext());
        mAsync.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mAsync != null) {
            mAsync.cancel(true);
        }
        return false;
    }

    private static class MyJobAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> weakContext;

        MyJobAsyncTask(Context context) {
            this.weakContext = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MovieService.syncAllDataMovies(weakContext.get());
            return null;
        }
    }
}
