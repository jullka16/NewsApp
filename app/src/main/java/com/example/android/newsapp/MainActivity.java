package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String category = sharedPrefs.getString(
                getString(R.string.settings_category_label),
                getString(R.string.default_category));

        Uri baseUri = Uri.parse(ARTICLES_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();


        uriBuilder.appendQueryParameter("q", category);
        Log.v("tag", "creating new request");

        return new ArticleLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        Log.v(LOG_TAG,"EarthquakeActivity onLoadFinished.");
        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_articles);

        adapter.clear();


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
