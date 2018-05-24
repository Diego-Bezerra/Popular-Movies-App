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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.api.MovieService;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProviderUtil;
import br.com.popularmoviesapp.popularmovies.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_MOVIE_ID = 1;
    public static final String EXTRA_MOVIE_ID = "android.intent.extra.EXTRA_MOVIE_ID";
    public static final String EXTRA_MOVIE_API_ID = "android.intent.extra.EXTRA_MOVIE_API_ID";

    private ActivityDetailsBinding mBinding;
    private int movieId;
    private int movieApiId;
    private MenuItem favoriteMenuItem;
    private boolean isFavorite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (getIntent().hasExtra(EXTRA_MOVIE_ID) && getIntent().hasExtra(EXTRA_MOVIE_API_ID)) {
            movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
            movieApiId = getIntent().getIntExtra(EXTRA_MOVIE_API_ID, 0);
            if (movieId > 0) {
                getSupportLoaderManager().initLoader(LOADER_MOVIE_ID, null, this);
            }
        }

        setupViewPager();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_menu, menu);
        favoriteMenuItem = menu.findItem(R.id.favorite);
        setFavoriteIcon(isFavorite, favoriteMenuItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                isFavorite = !isFavorite;
                setFavoriteIcon(isFavorite, item);
                MovieProviderUtil.updateFavorite(isFavorite, movieId, this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFavoriteIcon(boolean isFavorite, MenuItem menuItem) {
        menuItem.setIcon(isFavorite ? R.drawable.ic_fav : R.drawable.ic_unfav);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SynopsisFragment.newInstance(movieId), getString(R.string.synopsis));
        adapter.addFragment(VideosFragment.newInstance(movieId, movieApiId), getString(R.string.videos));
        adapter.addFragment(ReviewsFragment.newInstance(movieId, movieApiId), getString(R.string.reviews));
        mBinding.pager.setAdapter(adapter);
        mBinding.tabs.setupWithViewPager(mBinding.pager);
    }

    private void bindMovie(Cursor data) {

        if (data == null || data.getCount() == 0) return;

        data.moveToFirst();

        isFavorite = data.getInt(data.getColumnIndex(MovieContract.COLUMN_FAVORITE)) > 0;
        if (favoriteMenuItem != null) {
            setFavoriteIcon(isFavorite, favoriteMenuItem);
        }

        mBinding.imgMovieThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        byte[] img = data.getBlob(data.getColumnIndex(MovieContract.COLUMN_POSTER));
        if (img == null) {
            String posterPath = data.getString(data.getColumnIndex(MovieContract.COLUMN_POSTER_URL));
            Picasso.get().load(MovieService.getImageThumbPath(posterPath)).into(mBinding.imgMovieThumb);
        } else {
            Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
            mBinding.imgMovieThumb.setImageBitmap(bm);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case LOADER_MOVIE_ID:
                return MovieProviderUtil.getMovieById(movieId, this);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Cursor data) {
        bindMovie(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
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

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
