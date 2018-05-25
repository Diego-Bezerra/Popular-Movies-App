package br.com.popularmoviesapp.popularmovies.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import br.com.popularmoviesapp.popularmovies.R;
import br.com.popularmoviesapp.popularmovies.util.NetworkUtils;
import br.com.popularmoviesapp.popularmovies.util.ToastUtil;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkUtils.setNetworkListener(new NetworkUtils.NetworkListener() {
            @Override
            public void noInternetConnection() {
                ToastUtil.showToast(R.string.no_internet_connection, BaseActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkUtils.setNetworkListener(null);
    }
}
