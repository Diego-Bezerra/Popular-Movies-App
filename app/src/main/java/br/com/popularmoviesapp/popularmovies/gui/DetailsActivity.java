package br.com.popularmoviesapp.popularmovies.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import br.com.popularmoviesapp.popularmovies.R;

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
                Picasso.get().load(movie.posterPath).into(mMovieThumb);
                mOriginalTitle.setText(movie.originalTitle);
                mSynopsis.setText(movie.overview);
                mRating.setText(String.valueOf(String.valueOf(movie.voteAverage)));
                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
                mReleaseDate.setText(df.format(movie.releaseDate));
            }
        }
    }

    private String formattedText(int formatResource, String textToInsert) {
        return String.format(getString(formatResource), textToInsert);
    }

}
