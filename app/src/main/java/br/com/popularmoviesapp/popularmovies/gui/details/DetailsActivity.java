package br.com.popularmoviesapp.popularmovies.gui.details;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.api.MovieService;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProviderUtil;
import br.com.popularmoviesapp.popularmovies.data.review.ReviewProviderUtil;
import br.com.popularmoviesapp.popularmovies.data.video.VideoProviderUtil;
import br.com.popularmoviesapp.popularmovies.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String EXTRA_MOVIE_ID = "android.intent.extra.EXTRA_MOVIE_ID";
    public static final String EXTRA_MOVIE_API_ID = "android.intent.extra.EXTRA_MOVIE_API_ID";

    public static final int LOADER_DETAIL_ID = 1;
    public static final int LOADER_VIDEO_DETAIL_ID = 2;
    public static final int LOADER_REVIEW_DETAIL_ID = 3;

    private ActivityDetailsBinding mBinding;
    private int movieId;
    private int movieApiId;
    private boolean isFavorite;
    private boolean isLoadingMovie;
    private boolean isLoadingVideo;
    private boolean isLoadingReview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        mBinding.fabButton.setOnClickListener(this);

        if (getIntent().hasExtra(EXTRA_MOVIE_ID) && getIntent().hasExtra(EXTRA_MOVIE_API_ID)) {
            int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
            int movieApiId = getIntent().getIntExtra(EXTRA_MOVIE_API_ID, 0);
            if (movieId > 0 && movieApiId > 0) {
                mBinding.pgProgress.setVisibility(View.VISIBLE);
                this.movieId = movieId;
                this.movieApiId = movieApiId;
                getSupportLoaderManager().initLoader(LOADER_DETAIL_ID, null, this);
                getSupportLoaderManager().initLoader(LOADER_VIDEO_DETAIL_ID, null, this).forceLoad();
                getSupportLoaderManager().initLoader(LOADER_REVIEW_DETAIL_ID, null, this).forceLoad();
            }
        }

        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new VideosFragment(), getString(R.string.videos));
        adapter.addFragment(new ReviewsFragment(), getString(R.string.reviews));
        mBinding.innerLayout.pager.setAdapter(adapter);
        mBinding.innerLayout.tabs.setupWithViewPager(mBinding.innerLayout.pager);
    }

    private void setFavoriteIcon(boolean isFavorite) {
        mBinding.fabButton.setImageResource(isFavorite ? R.drawable.ic_fav : R.drawable.ic_unfav);
    }

    private void bindMovie(Cursor data) {

        if (data == null || data.getCount() == 0) return;

        data.moveToFirst();

        isFavorite = data.getInt(data.getColumnIndex(MovieContract.COLUMN_FAVORITE)) > 0;
        setFavoriteIcon(isFavorite);

        byte[] img = data.getBlob(data.getColumnIndex(MovieContract.COLUMN_POSTER));
        if (img == null) {
            String posterPath = data.getString(data.getColumnIndex(MovieContract.COLUMN_POSTER_URL));
            Picasso.get().load(MovieService.getImageThumbPath(posterPath)).into(mBinding.imgMovieThumb);
        } else {
            Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
            mBinding.imgMovieThumb.setImageBitmap(bm);
        }

        String title = data.getString(data.getColumnIndex(MovieContract.COLUMN_TITLE));
        double average = data.getDouble(data.getColumnIndex(MovieContract.COLUMN_AVERAGE));
        String synopsis = data.getString(data.getColumnIndex(MovieContract.COLUMN_SYNOPSIS));
        String dateStr = data.getString(data.getColumnIndex(MovieContract.COLUMN_RELEASE_DATE));
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.json_format_pattern), Locale.US);

        try {
            Date releaseDate = new Date(format.parse(dateStr).getTime());
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
            mBinding.innerLayout.tvReleaseDate.setText(df.format(releaseDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mBinding.innerLayout.tvOriginalTitle.setText(title);
        mBinding.innerLayout.tvSynopsis.setText(synopsis);
        mBinding.innerLayout.tvRating.setText(String.valueOf(String.valueOf(average)));
    }

    private void bindVideo(Cursor data) {
        if (data == null || data.getCount() == 0) return;
    }

    private void bindReview(Cursor data) {
        if (data == null || data.getCount() == 0) return;
    }

    private boolean isLoading() {
        return isLoadingMovie || isLoadingVideo || isLoadingReview;
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case LOADER_DETAIL_ID:
                isLoadingMovie = true;
                return MovieProviderUtil.getMovieById(movieId, this);
            case LOADER_VIDEO_DETAIL_ID:
                isLoadingVideo = true;
                return VideoProviderUtil.getVideosAsyncTaskLoaderByMovieId(movieId, movieApiId, this);
            case LOADER_REVIEW_DETAIL_ID:
                isLoadingReview = true;
                return ReviewProviderUtil.getReviewsAsyncTaskLoaderByMovieId(movieId, movieApiId, this);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_DETAIL_ID:
                isLoadingMovie = false;
                bindMovie(data);
                break;
            case LOADER_VIDEO_DETAIL_ID:
                isLoadingVideo = false;
                bindVideo(data);
                break;
            case LOADER_REVIEW_DETAIL_ID:
                isLoadingReview = false;
                bindReview(data);
                break;
            default:
                throw new RuntimeException("Loader Not Implemented: " + loader.getId());
        }

        if (!isLoading()) {
            mBinding.pgProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        isFavorite = !isFavorite;
        setFavoriteIcon(isFavorite);
        MovieProviderUtil.updateFavorite(isFavorite, movieId, this);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
