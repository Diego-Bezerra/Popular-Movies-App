package br.com.popularmoviesapp.popularmovies.gui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import br.com.popularmoviesapp.popularmovies.R;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    @SuppressWarnings("CanBeFinal")
    private Cursor mMoviesCursor;
    @SuppressWarnings("CanBeFinal")
    private MovieItemClickListener mMovieItemClickListener;

    public interface MovieItemClickListener {
        void onMovieClick(int movieId);
    }

    MovieListAdapter(Cursor movies, MovieItemClickListener movieItemClickListener) {
        this.mMoviesCursor = movies;
        this.mMovieItemClickListener = movieItemClickListener;
    }

    public void setmMoviesCursor(Cursor mMoviesCursor) {
        this.mMoviesCursor = mMoviesCursor;
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewHolder holder, int position) {
        mMoviesCursor.move(position);
        holder.bind(mMoviesCursor);
    }

    @Override
    public int getItemCount() {
        return mMoviesCursor.getCount();
    }

    public class MovieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @SuppressWarnings("CanBeFinal")
        ImageView movieThumb;

        MovieListViewHolder(View itemView) {
            super(itemView);
            movieThumb = itemView.findViewById(R.id.iv_movie_thumb);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int movieId = mMoviesCursor.getInt(MainActivity.INDEX_MOVIE_ID);
            mMovieItemClickListener.onMovieClick(movieId);
        }

        void bind(Cursor cursor) {
            byte[] img = cursor.getBlob(MainActivity.INDEX_MOVIE_POSTER);
            Bitmap bm = BitmapFactory.decodeByteArray(img, 0 ,img.length);
            movieThumb.setImageBitmap(bm);
        }
    }
}