package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Julia on 2018-03-17.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>>{

    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        return QueryUtils.fetchArticleData(mUrl);
    }
}
