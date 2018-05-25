package br.com.popularmoviesapp.popularmovies.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import br.com.popularmoviesapp.popularmovies.R;

public class PosterTarget implements Target {

    private ImageView mImageView;
    private Context context;
    private int movieId;

    public PosterTarget(ImageView mImageView, int movieId, Context context) {
        this.mImageView = mImageView;
        this.context = context;
        this.movieId = movieId;
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
//                MovieProviderUtil.updateMoviePoster(movieId, MovieContract.COLUMN_POSTER_THUMB, byteArray, context);
//            }
//        }).start();
//        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
        mImageView.setImageResource(R.drawable.ic_image);
    }
}