package br.com.popularmoviesapp.popularmovies.gui;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProvider;
import br.com.popularmoviesapp.popularmovies.sync.PopularMoviesSyncUtils;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String SORT_STATE = "sort_state";
    private static final int ID_MOVIES_LOADER = 10;

    private RecyclerView mMovieList;
    private ProgressBar mProgress;
    private TextView mNoResults;
    private MovieSortEnum mSelectedSort;
    private MovieListAdapter mAdapter;
    private MovieSortEnum selectedSort;

    public static final String[] MAIN_MOVIE_PROJECTION = {
            MovieContract._ID,
            MovieContract.COLUMN_TITLE,
            MovieContract.COLUMN_SYNOPSIS,
            MovieContract.COLUMN_POSTER,
            MovieContract.COLUMN_POSTER_URL,
            MovieContract.COLUMN_RELEASE_DATE,
            MovieContract.COLUMN_POPULARITY,
            MovieContract.COLUMN_FAVORITE
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_SYNOPSIS = 2;
    public static final int INDEX_MOVIE_POSTER = 3;
    public static final int INDEX_MOVIE_POSTER_URL = 4;
    public static final int INDEX_MOVIE_RELASE_DATE = 5;
    public static final int INDEX_MOVIE_POPULARITY = 6;
    public static final int INDEX_MOVIE_FAVORITE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = findViewById(R.id.rc_movie_list);
        mProgress = findViewById(R.id.pg_progress);
        mNoResults = findViewById(R.id.tv_no_results);

        selectedSort = MovieSortEnum.POPULAR;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_STATE)) {
                selectedSort = (MovieSortEnum) savedInstanceState.getSerializable(SORT_STATE);
            }
        }

        getSupportLoaderManager().initLoader(ID_MOVIES_LOADER, null, this);
        PopularMoviesSyncUtils.initialize(this);

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
                selectedSort = MovieSortEnum.POPULAR;
                break;
            case R.id.sort_action_top_rated:
                selectedSort = MovieSortEnum.TOP_RATED;
                break;
            case R.id.sort_action_favorites:
                selectedSort = MovieSortEnum.FAVORITE;
                break;
        }

        getSupportLoaderManager().initLoader(ID_MOVIES_LOADER, null, this);

        return super.onOptionsItemSelected(item);
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
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieClick(int movieId) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movieId);
        startActivity(intent);
    }

    private void setupMovieList(Cursor cursor) {

        if (cursor == null || cursor.getCount() == 0) return;

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
            mAdapter = new MovieListAdapter(cursor, this);
            mMovieList.setAdapter(mAdapter);
        } else {
            mAdapter.swipeData(cursor);
            mAdapter.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_MOVIES_LOADER:
                mNoResults.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                mMovieList.setAdapter(null);
                return MovieProvider.getAllMoviesCursorLoader(selectedSort, this);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        mProgress.setVisibility(View.INVISIBLE);

        if (data != null && data.getCount() > 0) {
            showNoResults(true);
            setupMovieList(data);
        } else {
            showNoResults(false);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
