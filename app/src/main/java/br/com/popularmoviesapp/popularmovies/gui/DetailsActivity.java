package br.com.popularmoviesapp.popularmovies.gui;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.api.VideoService;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProviderUtil;
import br.com.popularmoviesapp.popularmovies.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_MOVIE_ID = "android.intent.extra.EXTRA_MOVIE_ID";
    public static final String EXTRA_MOVIE_API_ID = "android.intent.extra.EXTRA_MOVIE_API_ID";

    public static final int LOADER_DETAIL_ID = 1;
    public static final int LOADER_VIDEO_REVIEW_DETAIL_ID = 2;

    private ActivityDetailsBinding mBinding;
    private int movieId;
    private int movieApiId;
    private boolean initialized;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        if (getIntent().hasExtra(EXTRA_MOVIE_ID) && getIntent().hasExtra(EXTRA_MOVIE_API_ID)) {
            int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
            int movieApiId = getIntent().getIntExtra(EXTRA_MOVIE_API_ID, 0);
            if (movieId > 0 && movieApiId > 0) {
                this.movieId = movieId;
                this.movieApiId = movieApiId;
                getSupportLoaderManager().initLoader(LOADER_DETAIL_ID, null, this);
            }
        }
    }

    private void bindLoader(Cursor data) {
        data.moveToNext();

        byte[] img = data.getBlob(data.getColumnIndex(MovieContract.COLUMN_POSTER));
        if (img == null) {
            String posterPath = data.getString(data.getColumnIndex(MovieContract.COLUMN_POSTER_URL));
            Picasso.get().load(posterPath).into(mBinding.imgMovieThumb);
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

        data.close();
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case LOADER_DETAIL_ID:
                return MovieProviderUtil.getMovieById(movieId, this);
                break;
            case LOADER_VIDEO_REVIEW_DETAIL_ID:
                return
                break;
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_DETAIL_ID:
                if (data != null && data.getCount() > 0) {
                    bindLoader(data);
                } else if (!initialized) {
                    initialized = true;
                    getSupportLoaderManager().initLoader(LOADER_VIDEO_REVIEW_DETAIL_ID, null, this);
                }
            break;
            case LOADER_VIDEO_REVIEW_DETAIL_ID:

                break;
            default:
                throw new RuntimeException("Loader Not Implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
