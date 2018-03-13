package br.com.popularmoviesapp.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieItemClickListener
        , AdapterView.OnItemSelectedListener {

    private RecyclerView mMovieList;
    private ProgressBar mProgress;
    private Spinner mMovieSort;
    private TextView mNoResults;

    private enum MovieSort {
        POPULAR(R.string.popular),
        TOP_RATED(R.string.top_rated);

        @SuppressWarnings("CanBeFinal")
        private int strValue;

        MovieSort(int strValue) {
            this.strValue = strValue;
        }

        public int getStrValue() {
            return strValue;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = findViewById(R.id.rc_movie_list);
        mProgress = findViewById(R.id.pg_progress);
        mMovieSort = findViewById(R.id.sp_movie_sort);
        mNoResults = findViewById(R.id.tv_no_results);

        setupMovieSortSpinner();
    }

    private void setupMovieSortSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this
                , R.layout.movie_sort_spinner_item
                , R.id.tv_text
                , new String[]{
                getString(MovieSort.POPULAR.getStrValue())
                , getString(MovieSort.TOP_RATED.getStrValue())});

        mMovieSort.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        mMovieSort.setAdapter(adapter);
        mMovieSort.setOnItemSelectedListener(this);
    }

    private void executeMovieAsyncTask() {

        if (!NetworkUtils.isNetworkAvailable(this)) {

            Toast.makeText(this
                    , this.getText(R.string.no_internet)
                    , Toast.LENGTH_LONG).show();
        }

        MovieSort movieSort = MovieSort.values()[mMovieSort.getSelectedItemPosition()];
        new MoviesAsyncTask(this).execute(movieSort);
    }

    private void showNoResults(boolean show) {
        if (show) {
            mMovieList.setVisibility(View.VISIBLE);
            mNoResults.setVisibility(View.GONE);
        } else {
            mMovieList.setVisibility(View.INVISIBLE);
            mNoResults.setVisibility(View.VISIBLE);
        }
    }

    public static class MoviesAsyncTask extends AsyncTask<MovieSort, Void, ArrayList<MovieResponse>> {

        @SuppressWarnings("CanBeFinal")
        private WeakReference<MainActivity> activityReference;

        public MoviesAsyncTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MainActivity activity = activityReference.get();

            activity.mNoResults.setVisibility(View.GONE);
            activity.mProgress.setVisibility(View.VISIBLE);
            activity.mMovieList.setAdapter(null);
        }

        @Override
        protected ArrayList<MovieResponse> doInBackground(MovieSort... movieSorts) {
            MovieSort movieSort = movieSorts[0];
            switch (movieSort) {
                case POPULAR:
                    return MovieApiService.getPopularMovies();
                case TOP_RATED:
                    return MovieApiService.getTopRatedMovies();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieResponse> movies) {

            MainActivity activity = activityReference.get();

            activity.mProgress.setVisibility(View.INVISIBLE);

            if (movies.size() > 0) {

                activity.showNoResults(true);

                int spanCount = 2;
                if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    spanCount = 3;
                }

                GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, spanCount);
                activity.mMovieList.setHasFixedSize(true);
                activity.mMovieList.setLayoutManager(gridLayoutManager);
                activity.mMovieList.setAdapter(new MovieListAdapter(movies, activity));
            } else {
                activity.showNoResults(false);
            }

            super.onPostExecute(movies);
        }
    }

    @Override
    public void onMovieClick(MovieResponse movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MovieSort movieSort = MovieSort.values()[position];
        switch (movieSort) {
            case POPULAR:
                executeMovieAsyncTask();
                break;
            case TOP_RATED:
                executeMovieAsyncTask();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
