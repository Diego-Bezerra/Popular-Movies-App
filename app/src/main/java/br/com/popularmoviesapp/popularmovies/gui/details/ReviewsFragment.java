package br.com.popularmoviesapp.popularmovies.gui.details;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.data.review.ReviewProviderUtil;
import br.com.popularmoviesapp.popularmovies.databinding.FragmentReviewsBinding;

public class ReviewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_MOVIE_ID = "android.intent.extra.EXTRA_MOVIE_ID";
    public static final String EXTRA_MOVIE_API_ID = "android.intent.extra.EXTRA_MOVIE_API_ID";
    public static final int LOADER_REVIEW_DETAIL_ID = 3;

    private int movieId;
    private int movieApiId;
    private ReviewAdapter adapter;
    private FragmentReviewsBinding mBinding;

    public static ReviewsFragment newInstance(int movieId, int movieApiId) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MOVIE_ID, movieId);
        bundle.putInt(EXTRA_MOVIE_API_ID, movieApiId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (getActivity() != null && bundle != null && bundle.containsKey(EXTRA_MOVIE_ID)) {
            movieId = bundle.getInt(EXTRA_MOVIE_ID);
            movieApiId = bundle.getInt(EXTRA_MOVIE_API_ID);
        }
    }

    private void setupList(Cursor data) {
        if (data != null && data.getCount() > 0) {
            adapter.swipeData(data);
            mBinding.tvNoResults.setVisibility(View.GONE);
            mBinding.reviewsList.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvNoResults.setVisibility(View.VISIBLE);
            mBinding.reviewsList.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false);

        mBinding.reviewsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.reviewsList.setHasFixedSize(true);
        adapter = new ReviewAdapter();
        mBinding.reviewsList.setAdapter(adapter);

        if (getActivity() != null) {
            LoaderManager lm = getLoaderManager();
            lm.initLoader(LOADER_REVIEW_DETAIL_ID, null, this);
        }
        return mBinding.getRoot();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case LOADER_REVIEW_DETAIL_ID:
                mBinding.pgProgress.setVisibility(View.VISIBLE);
                return ReviewProviderUtil.getReviewsAsyncTaskLoaderByMovieId(movieId, movieApiId, getActivity());
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mBinding.pgProgress.setVisibility(View.GONE);
        setupList(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
