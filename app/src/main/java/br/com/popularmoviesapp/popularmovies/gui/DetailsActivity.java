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
import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProvider;
import br.com.popularmoviesapp.popularmovies.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_MOVIE_ID = "android.intent.extra.EXTRA_MOVIE_ID";
    public static final int LOADER_DETAIL_ID = 1;

    private ActivityDetailsBinding mBinding;
    private int movieId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        if (getIntent().hasExtra(EXTRA_MOVIE_ID)) {
            int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
            if (movieId > 0) {
                this.movieId = movieId;
                getSupportLoaderManager().initLoader(LOADER_DETAIL_ID, null, this);
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return MovieProvider.getMovieById(movieId, this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {

            data.moveToNext();

            byte[] img = data.getBlob(data.getColumnIndex(MovieContract.COLUMN_POSTER));
            if (img == null) {
                String posterPath = data.getString(data.getColumnIndex(MovieContract.COLUMN_POSTER_URL));
                Picasso.get().load(posterPath).into(new PosterTarget(mBinding.imgMovieThumb, movieId, this));
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
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
