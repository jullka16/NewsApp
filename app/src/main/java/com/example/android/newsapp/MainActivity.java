package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>{
    private static final int ARTICLES_LOADER_ID = 1;
    private static final String ARTICLES_REQUEST_URL = "http://content.guardianapis.com/search?q=pokemon&api-key=test&show-tags=contributor";
    public static final String LOG_TAG = MainActivity.class.getName();
    ArticleAdapter adapter;

    private TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        ListView list = (ListView) findViewById(R.id.list);
        adapter = new ArticleAdapter(this, 0, new ArrayList<Article>());
        list.setAdapter(adapter);

        list.setEmptyView(mEmptyStateTextView);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info != null && info.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLES_LOADER_ID, null, this);
        } else{
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);

        }

    }


    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG,"EarthquakeActivity onCreateLoader");
        return new ArticleLoader(this, ARTICLES_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        adapter.clear();
        Log.v(LOG_TAG,"EarthquakeActivity onLoadFinished.");
        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_articles);


        if(articles != null && !articles.isEmpty()){
            adapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        Log.v(LOG_TAG,"EarthquakeActivity onLoadReset.");
        adapter.clear();
    }
}
