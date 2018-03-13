package br.com.popularmoviesapp.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "android.intent.extra.EXTRA_MOVIE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageView mMovieThumb = findViewById(R.id.img_movie_thumb);
        TextView mOriginalTitle = findViewById(R.id.tv_original_title);
        TextView mSynopsis = findViewById(R.id.tv_synopsis);
        TextView mRating = findViewById(R.id.tv_rating);
        TextView mReleaseDate = findViewById(R.id.tv_release_date);

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            MovieResponse movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            Picasso.get().load(movie.posterPath).into(mMovieThumb);
            mOriginalTitle.setText(formattedText(R.string.format_title, movie.originalTitle));
            mSynopsis.setText(movie.overview);
            mRating.setText(String.valueOf(formattedText(R.string.format_rating, String.valueOf(movie.voteAverage))));
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            mReleaseDate.setText(formattedText(R.string.format_release_date, df.format(movie.releaseDate)));
        }
    }

    private String formattedText(int formatResource, String textToInsert) {
        return String.format(getString(formatResource), textToInsert);
    }

}
