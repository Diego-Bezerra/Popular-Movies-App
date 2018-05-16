package br.com.popularmoviesapp.popularmovies.sync;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.lang.ref.WeakReference;

import br.com.popularmoviesapp.popularmovies.api.MovieService;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProvider;
import br.com.popularmoviesapp.popularmovies.gui.MovieSortEnum;

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
            MovieService.syncMoviesData(this.weakContext.get());
            Cursor cursor = MovieProvider.getAllMoviesCursor(MovieSortEnum.POPULAR, this.weakContext.get());
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int movieId = cursor.getInt(cursor.getColumnIndex(MovieContract._ID));

                }
            }
            return null;
        }
    }
}
