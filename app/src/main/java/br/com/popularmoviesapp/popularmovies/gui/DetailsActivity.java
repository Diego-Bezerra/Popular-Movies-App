package br.com.popularmoviesapp.popularmovies.gui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProvider;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "android.intent.extra.EXTRA_MOVIE_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageView mMovieThumb = findViewById(R.id.img_movie_thumb);
        TextView mOriginalTitle = findViewById(R.id.tv_original_title);
        TextView mSynopsis = findViewById(R.id.tv_synopsis);
        TextView mRating = findViewById(R.id.tv_rating);
        TextView mReleaseDate = findViewById(R.id.tv_release_date);

        if (getIntent().hasExtra(EXTRA_MOVIE_ID)) {
            int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
            if (movieId > 0) {

                Cursor cursor = MovieProvider.getMovieById(movieId, this);
                if (cursor != null && cursor.getCount() > 0) {

                    String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_POSTER));
                    String title = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_TITLE));
                    double average = cursor.getDouble(cursor.getColumnIndex(MovieContract.COLUMN_AVERAGE));
                    String synopsis = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_SYNOPSIS));
                    String dateStr = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_RELEASE_DATE));
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                    try {
                        Date releaseDate = new Date(format.parse(dateStr).getTime());
                        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
                        mReleaseDate.setText(df.format(releaseDate));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Picasso.get().load(posterPath).into(new PosterTarget(mMovieThumb, movieId, this));
                    mOriginalTitle.setText(title);
                    mSynopsis.setText(synopsis);
                    mRating.setText(String.valueOf(String.valueOf(average)));
                }
            }
        }
    }

    private String formattedText(int formatResource, String textToInsert) {
        return String.format(getString(formatResource), textToInsert);
    }
}
