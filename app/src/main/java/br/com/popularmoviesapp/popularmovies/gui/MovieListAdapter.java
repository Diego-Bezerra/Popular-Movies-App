package br.com.popularmoviesapp.popularmovies.gui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.network.MovieResponse;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    @SuppressWarnings("CanBeFinal")
    private ArrayList<MovieResponse> mMovies;
    @SuppressWarnings("CanBeFinal")
    private MovieItemClickListener mMovieItemClickListener;

    public interface MovieItemClickListener {
        void onMovieClick(MovieResponse movie);
    }

    MovieListAdapter(ArrayList<MovieResponse> movies, MovieItemClickListener movieItemClickListener) {
        this.mMovies = movies;
        this.mMovieItemClickListener = movieItemClickListener;
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
        MovieResponse movie = mMovies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
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
            MovieResponse movie = mMovies.get(getAdapterPosition());
            mMovieItemClickListener.onMovieClick(movie);
        }

        void bind(MovieResponse movie) {
            Picasso.get().load(movie.posterPath).into(movieThumb);
        }
    }
}
