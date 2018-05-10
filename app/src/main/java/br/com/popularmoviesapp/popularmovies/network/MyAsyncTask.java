package br.com.popularmoviesapp.popularmovies.network;

import android.os.AsyncTask;

import br.com.popularmoviesapp.popularmovies.util.LogUtil;

public class MyAsyncTask<T, Z, J> extends AsyncTask<T, Z, J> {

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
        LogUtil.logInfo("onPreExecute");
    }

    @Override
    protected J doInBackground(T[] ts) {
        LogUtil.logInfo("doInBackground");
        return mAsyncTaskListener.doInBackground(ts);
    }

    @Override
    protected void onPostExecute(J j) {
        LogUtil.logInfo("onPostExecute");
        mAsyncTaskListener.onPostExecute(j);
    }
}