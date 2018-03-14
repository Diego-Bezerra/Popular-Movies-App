package br.com.popularmoviesapp.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieItemClickListener
        , AdapterView.OnItemSelectedListener, MyAsyncTask.AsyncTaskListener<MovieSortEnum, ArrayList<MovieResponse>> {

    private RecyclerView mMovieList;
    private ProgressBar mProgress;
    private Spinner mMovieSort;
    private TextView mNoResults;

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

        ArrayList<String> sortTypes = new ArrayList<>();
        for (MovieSortEnum sortEnum : MovieSortEnum.values()) {
            sortTypes.add(sortEnum.getStrValue(this));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this
                , R.layout.movie_sort_spinner_item
                , R.id.tv_text
                , sortTypes);

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

        MovieSortEnum movieSort = MovieSortEnum.values()[mMovieSort.getSelectedItemPosition()];
        new MyAsyncTask<MovieSortEnum, Void, ArrayList<MovieResponse>>(this).execute(movieSort);
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

    @Override
    public void onMovieClick(MovieResponse movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MovieSortEnum movieSort = MovieSortEnum.values()[position];
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

    @Override
    public void onPreExecute() {
        mNoResults.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
        mMovieList.setAdapter(null);
    }

    @Override
    public ArrayList<MovieResponse> doInBackground(MovieSortEnum[] params) {
        MovieSortEnum sortEnum = params[0];
        switch (sortEnum) {
            case POPULAR:
                return MovieApiService.getPopularMovies();
            case TOP_RATED:
                return MovieApiService.getTopRatedMovies();
        }
        return null;
    }

    @Override
    public void onPostExecute(ArrayList<MovieResponse> parameter) {

        mProgress.setVisibility(View.INVISIBLE);

        if (parameter.size() > 0) {

            showNoResults(true);

            int spanCount = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = 3;
            }

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            mMovieList.setHasFixedSize(true);
            mMovieList.setLayoutManager(gridLayoutManager);
            mMovieList.setAdapter(new MovieListAdapter(parameter, this));
        } else {
            showNoResults(false);
        }
    }
}
