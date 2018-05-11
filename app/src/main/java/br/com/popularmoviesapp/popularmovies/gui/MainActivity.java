package br.com.popularmoviesapp.popularmovies.gui;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import br.com.popularmoviesapp.popularmovies.data.MovieContract;
import br.com.popularmoviesapp.popularmovies.network.MovieApiService;
import br.com.popularmoviesapp.popularmovies.network.MovieResponse;
import br.com.popularmoviesapp.popularmovies.network.MyAsyncTask;
import br.com.popularmoviesapp.popularmovies.network.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieItemClickListener
        , MyAsyncTask.AsyncTaskListener<MovieSortEnum, ArrayList<MovieResponse>>, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LIST_STATE = "list_state";
    private static final String SORT_STATE = "sort_state";
    private static final String MOVIE_LIST_RESPONSE_STATE = "moview_list_response_state";
    private static final String ALREADY_MADE_QUERY_STATE = "already_made_query_state";
    private static final int ID_MOVIES_LOADER = 10;

    private RecyclerView mMovieList;
    private ProgressBar mProgress;
    private TextView mNoResults;
    private MovieSortEnum mSelectedSort;
    private MovieListAdapter mAdapter;
    private ArrayList<MovieResponse> mMovieListResponse;

    public static final String[] MAIN_MOVIE_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_FAVORITE
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_SYNOPSIS = 2;
    public static final int INDEX_MOVIE_POSTER = 3;
    public static final int INDEX_MOVIE_RELASE_DATE = 4;
    public static final int INDEX_MOVIE_POPULARITY = 5;
    public static final int INDEX_MOVIE_FAVORITE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = findViewById(R.id.rc_movie_list);
        mProgress = findViewById(R.id.pg_progress);
        mNoResults = findViewById(R.id.tv_no_results);

        MovieSortEnum sort = MovieSortEnum.POPULAR;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_STATE)) {
                sort = (MovieSortEnum) savedInstanceState.getSerializable(SORT_STATE);
            }
            if (savedInstanceState.containsKey(MOVIE_LIST_RESPONSE_STATE)) {
                mMovieListResponse = savedInstanceState.getParcelableArrayList(MOVIE_LIST_RESPONSE_STATE);
            }
        }

        setupMovieList(mMovieListResponse);
        getSupportLoaderManager().initLoader(ID_MOVIES_LOADER, null, this);
//        if (mMovieListResponse == null) {
//            executeMovieAsyncTask(sort);
//        }

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

        mSelectedSort = movieSort;
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
        outState.putSerializable(SORT_STATE, mSelectedSort);
        outState.putParcelableArrayList(MOVIE_LIST_RESPONSE_STATE, mMovieListResponse);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieClick(int movieId) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, movieId);
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
            mMovieListResponse = parameter;
            showNoResults(true);
            setupMovieList(parameter);
        } else {
            showNoResults(false);
        }
    }

    private void setupMovieList(ArrayList<MovieResponse> movieResponse) {

        if (movieResponse == null) return;

        final int SW_TABLET = 600;

        if (mMovieList.getAdapter() == null) {
            Configuration config = getResources().getConfiguration();
            int spanCount = 2;
            if (config.smallestScreenWidthDp >= SW_TABLET || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = 3;
            }

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            mMovieList.setHasFixedSize(true);
            mMovieList.setLayoutManager(gridLayoutManager);
            mAdapter = new MovieListAdapter(movieResponse, this);
            mMovieList.setAdapter(mAdapter);
        } else {
            mAdapter.setMovies(movieResponse);
            mAdapter.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_MOVIES_LOADER:
                return new CursorLoader(MainActivity.this
                        , MovieContract.MovieEntry.CONTENT_URI
                        , MAIN_MOVIE_PROJECTION
                        , null
                        , null
                        , MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC");
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
