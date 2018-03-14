package br.com.popularmoviesapp.popularmovies;

import android.os.AsyncTask;

class MyAsyncTask<T, Z, J> extends AsyncTask<T, Z, J> {

    private final AsyncTaskListener<T, J> mAsyncTaskListener;

    public interface AsyncTaskListener<T, J> {
        void onPreExecute();

        J doInBackground(T[] params);

        void onPostExecute(J parameter);
    }

    public MyAsyncTask(AsyncTaskListener<T, J> asyncTaskListener) {
        mAsyncTaskListener = asyncTaskListener;
    }

    @Override
    protected void onPreExecute() {
        mAsyncTaskListener.onPreExecute();
    }

    @Override
    protected J doInBackground(T[] ts) {
        return mAsyncTaskListener.doInBackground(ts);
    }

    @Override
    protected void onPostExecute(J j) {
        mAsyncTaskListener.onPostExecute(j);
    }
}