package br.com.popularmoviesapp.popularmovies.gui.details;

import android.content.ActivityNotFoundException;
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
import br.com.popularmoviesapp.popularmovies.data.video.VideoContract;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> implements View.OnClickListener {

    private Cursor mCursor;

    VideoAdapter(Cursor data) {
        this.mCursor = data;
    }

    VideoAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.infoContainer.setOnClickListener(this);
        holder.btnShare.setOnClickListener(this);
        holder.bind(mCursor);
    }

    public void swipeData(Cursor data) {
        mCursor = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    @Override
    public void onClick(View v) {
        String key = mCursor.getString(mCursor.getColumnIndex(VideoContract.COLUMN_KEY));
        switch (v.getId()) {
            case R.id.info_container:
                watchYoutubeVideo(v.getContext(), key);
                break;
            case R.id.btn_share:
                String name = mCursor.getString(mCursor.getColumnIndex(VideoContract.COLUMN_NAME));
                shareVideo(v.getContext(), key, name);
                break;
        }
    }

    private void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    private void shareVideo(Context context, String id, String name) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                name + ": http://www.youtube.com/watch?v=" + id);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup infoContainer;
        private TextView name;
        private TextView type;
        private AppCompatImageButton btnShare;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            infoContainer = itemView.findViewById(R.id.info_container);
            btnShare = itemView.findViewById(R.id.btn_share);
        }

        void bind(Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex(VideoContract.COLUMN_NAME));
            String type = cursor.getString(cursor.getColumnIndex(VideoContract.COLUMN_TYPE));
            this.name.setText(name);
            this.type.setText(type);
        }
    }
}
