package br.com.popularmoviesapp.popularmovies.gui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.api.MovieService;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieContract;
import br.com.popularmoviesapp.popularmovies.data.movie.MovieProviderUtil;

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

    public void swipeData(Cursor mMoviesCursor) {
        this.mMoviesCursor = mMoviesCursor;
        this.notifyDataSetChanged();
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
        mMoviesCursor.moveToPosition(position);
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
            mMoviesCursor.moveToPosition(this.getLayoutPosition());
            int movieId = mMoviesCursor.getInt(mMoviesCursor.getColumnIndex(MovieContract._ID));
            mMovieItemClickListener.onMovieClick(movieId);
        }

        void bind(Cursor cursor) {
            byte[] img = cursor.getBlob(cursor.getColumnIndex(MovieContract.COLUMN_POSTER));
            if (img == null) {
                final int movieId = cursor.getInt(cursor.getColumnIndex(MovieContract._ID));
                final String urlEnd = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_POSTER_URL));
                final Context context = this.itemView.getContext();

                Picasso.get().load(MovieService.getImageThumbPath(urlEnd)).into(movieThumb);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = ((BitmapDrawable)movieThumb.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        MovieProviderUtil.updateMoviePoster(movieId, byteArray, context);
                    }
                }).start();

            } else {
                Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                movieThumb.setImageBitmap(bm);

            }
        }
    }
}
