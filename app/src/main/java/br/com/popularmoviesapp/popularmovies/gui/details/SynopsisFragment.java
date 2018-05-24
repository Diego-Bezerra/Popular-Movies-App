package br.com.popularmoviesapp.popularmovies.gui.details;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProviderUtil;
import br.com.popularmoviesapp.popularmovies.databinding.FragmentSynopsisBinding;

public class SynopsisFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_MOVIE_ID = "android.intent.extra.EXTRA_MOVIE_ID";
    public static final int LOADER_MOVIE_ID = 2;
    private int movieId;
    private FragmentSynopsisBinding mBinding;

    public static SynopsisFragment newInstance(int movieId) {
        SynopsisFragment fragment = new SynopsisFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MOVIE_ID, movieId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EXTRA_MOVIE_ID)) {
            movieId = bundle.getInt(EXTRA_MOVIE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_synopsis, container, false);
        if (getActivity() != null) {
            LoaderManager lm = getLoaderManager();
            lm.initLoader(LOADER_MOVIE_ID, null, this);
        }
        return mBinding.getRoot();
    }

    private void bindMovie(Cursor data) {

        if (data == null || data.getCount() == 0) return;

        data.moveToFirst();

        String title = data.getString(data.getColumnIndex(MovieContract.COLUMN_TITLE));
        double average = data.getDouble(data.getColumnIndex(MovieContract.COLUMN_AVERAGE));
        String synopsis = data.getString(data.getColumnIndex(MovieContract.COLUMN_SYNOPSIS));
        String dateStr = data.getString(data.getColumnIndex(MovieContract.COLUMN_RELEASE_DATE));
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.json_format_pattern), Locale.US);

        try {
            Date releaseDate = new Date(format.parse(dateStr).getTime());
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
            mBinding.tvReleaseDate.setText(df.format(releaseDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mBinding.tvOriginalTitle.setText(title);
        mBinding.tvSynopsis.setText(synopsis);
        mBinding.tvRating.setText(String.valueOf(String.valueOf(average)));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case LOADER_MOVIE_ID:
                mBinding.pgProgress.setVisibility(View.VISIBLE);
                return MovieProviderUtil.getMovieById(movieId, getActivity());
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mBinding.pgProgress.setVisibility(View.GONE);
        bindMovie(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
