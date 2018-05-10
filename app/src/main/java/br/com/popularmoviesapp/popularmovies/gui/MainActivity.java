package br.com.popularmoviesapp.popularmovies.gui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.network.MovieApiService;
import br.com.popularmoviesapp.popularmovies.network.MovieResponse;
import br.com.popularmoviesapp.popularmovies.network.MyAsyncTask;
import br.com.popularmoviesapp.popularmovies.network.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieItemClickListener
        , MyAsyncTask.AsyncTaskListener<MovieSortEnum, ArrayList<MovieResponse>> {

    private static final String LIST_STATE = "list_state";
    private static final String SORT_STATE = "sort_state";

    private RecyclerView mMovieList;
    private ProgressBar mProgress;
    private TextView mNoResults;
    private MovieSortEnum selectedSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = findViewById(R.id.rc_movie_list);
        mProgress = findViewById(R.id.pg_progress);
        mNoResults = findViewById(R.id.tv_no_results);

        MovieSortEnum sort = MovieSortEnum.POPULAR;
        if (savedInstanceState != null && savedInstanceState.containsKey(SORT_STATE)) {
            sort = (MovieSortEnum) savedInstanceState.getSerializable(SORT_STATE);
        }
        executeMovieAsyncTask(sort);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_action_popular:
                executeMovieAsyncTask(MovieSortEnum.POPULAR);
                break;
            case R.id.sort_action_top_rated:
                executeMovieAsyncTask(MovieSortEnum.TOP_RATED);
                break;
            case R.id.sort_action_favorites:
                //executeMovieAsyncTask(MovieSortEnum.FAVORITE);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void executeMovieAsyncTask(MovieSortEnum movieSort) {

        selectedSort = movieSort;
        if (!NetworkUtils.isNetworkAvailable(this)) {

            Toast.makeText(this
                    , this.getText(R.string.no_internet)
                    , Toast.LENGTH_LONG).show();
            return;
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LIST_STATE, mMovieList.getLayoutManager().onSaveInstanceState());
        outState.putSerializable(SORT_STATE, selectedSort);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState.containsKey(LIST_STATE)) {
//            Parcelable listState = savedInstanceState.getParcelable(LIST_STATE);
//            mMovieList.getLayoutManager().onRestoreInstanceState(listState);
//        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onMovieClick(MovieResponse movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
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
