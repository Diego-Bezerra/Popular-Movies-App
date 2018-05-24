package br.com.popularmoviesapp.popularmovies.gui.details;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.data.review.ReviewContract;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> implements View.OnClickListener {

    private Cursor mCursor;

    ReviewAdapter() {
    }

    public void swipeData(Cursor data) {
        mCursor = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_review, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        //holder.infoContainer.setOnClickListener(this);
        holder.btnShare.setOnClickListener(this);
        holder.bind(mCursor);
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    @Override
    public void onClick(View v) {
        String url = mCursor.getString(mCursor.getColumnIndex(ReviewContract.COLUMN_URL));
        switch (v.getId()) {
//            case R.id.info_container:
//                openReviewLink(v.getContext(), url);
//                break;
            case R.id.btn_share:
                String author = mCursor.getString(mCursor.getColumnIndex(ReviewContract.COLUMN_AUTHOR));
                shareLinkContent(v.getContext(), author, url);
                break;
        }
    }

    private void openReviewLink(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    private void shareLinkContent(Context context, String author, String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Review written by " + author + ": " + url);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup infoContainer;
        private TextView content;
        private TextView author;
        private AppCompatImageButton btnShare;

        ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            author = itemView.findViewById(R.id.author);
            infoContainer = itemView.findViewById(R.id.info_container);
            btnShare = itemView.findViewById(R.id.btn_share);
        }

        void bind(Cursor cursor) {
            String content = cursor.getString(cursor.getColumnIndex(ReviewContract.COLUMN_CONTENT));
            String author = cursor.getString(cursor.getColumnIndex(ReviewContract.COLUMN_AUTHOR));
            this.content.setText(content);
            this.author.setText(author);
        }
    }
}
