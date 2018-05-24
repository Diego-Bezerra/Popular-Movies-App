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
import br.com.popularmoviesapp.popularmovies.data.video.VideoProviderUtil;
import br.com.popularmoviesapp.popularmovies.databinding.FragmentVideosBinding;

public class VideosFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_MOVIE_ID = "android.intent.extra.EXTRA_MOVIE_ID";
    public static final String EXTRA_MOVIE_API_ID = "android.intent.extra.EXTRA_MOVIE_API_ID";
    public static final int LOADER_VIDEO_DETAIL_ID = 3;

    private int movieId;
    private int movieApiId;
    private VideoAdapter adapter;
    private FragmentVideosBinding mBinding;

    public static VideosFragment newInstance(int movieId, int movieApiId) {
        VideosFragment fragment = new VideosFragment();
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
        if (getActivity() != null && bundle != null && bundle.containsKey(EXTRA_MOVIE_API_ID)) {
            movieId = bundle.getInt(EXTRA_MOVIE_ID);
            movieApiId = bundle.getInt(EXTRA_MOVIE_API_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos, container, false);

        mBinding.videosList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.videosList.setHasFixedSize(true);
        adapter = new VideoAdapter();
        mBinding.videosList.setAdapter(adapter);

        if (getActivity() != null) {
            LoaderManager lm = getLoaderManager();
            lm.initLoader(LOADER_VIDEO_DETAIL_ID, null, this);
        }
        return mBinding.getRoot();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case LOADER_VIDEO_DETAIL_ID:
                mBinding.pgProgress.setVisibility(View.VISIBLE);
                return VideoProviderUtil.getVideosAsyncTaskLoaderByMovieId(movieId, movieApiId, getActivity());
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        setupRecyclerView(data);
        mBinding.pgProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void setupRecyclerView(Cursor data) {
        if (data != null) {
            adapter.swipeData(data);
            mBinding.videosList.setVisibility(View.VISIBLE);
            mBinding.tvNoResults.setVisibility(View.GONE);
        } else {
            mBinding.videosList.setVisibility(View.GONE);
            mBinding.tvNoResults.setVisibility(View.VISIBLE);
        }
    }
}
