package br.com.popularmoviesapp.popularmovies.gui;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProvider;
import br.com.popularmoviesapp.popularmovies.databinding.ActivityMainBinding;
import br.com.popularmoviesapp.popularmovies.sync.PopularMoviesSyncUtils;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String SORT_STATE = "sort_state";
    private static final int ID_MOVIES_LOADER = 10;

    private ActivityMainBinding mainBinding;
    private MovieListAdapter mAdapter;
    private MovieSortEnum selectedSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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
        MovieSortEnum oldSelected = selectedSort;
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

        if (oldSelected != selectedSort) {
            getSupportLoaderManager().restartLoader(ID_MOVIES_LOADER, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNoResults(boolean show) {
        if (show) {
            mainBinding.rcMovieList.setVisibility(View.VISIBLE);
            mainBinding.tvNoResults.setVisibility(View.GONE);
        } else {
            mainBinding.rcMovieList.setVisibility(View.INVISIBLE);
            mainBinding.tvNoResults.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SORT_STATE, selectedSort);
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

        if (mainBinding.rcMovieList.getAdapter() == null) {
            Configuration config = getResources().getConfiguration();
            int spanCount = 2;
            if (config.smallestScreenWidthDp >= SW_TABLET || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = 3;
            }

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
            mainBinding.rcMovieList.setHasFixedSize(true);
            mainBinding.rcMovieList.setLayoutManager(gridLayoutManager);
            mAdapter = new MovieListAdapter(cursor, this);
            mainBinding.rcMovieList.setAdapter(mAdapter);
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
                mainBinding.tvNoResults.setVisibility(View.GONE);
                mainBinding.pgProgress.setVisibility(View.VISIBLE);
                mainBinding.rcMovieList.setAdapter(null);
                return MovieProvider.getAllMoviesCursorLoader(selectedSort, this);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        mainBinding.pgProgress.setVisibility(View.INVISIBLE);

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
