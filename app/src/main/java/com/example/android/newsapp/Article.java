package com.example.android.newsapp;

/**
 * Created by Julia on 2018-03-10.
 */

public class Article {
    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mDate;
    private String mUrl;

    Article(String title, String section, String author, String date, String url){
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mUrl = url;
    }

    Article(String title, String section, String date, String url){
        mTitle = title;
        mSection = section;
        mDate = date;
        mUrl = url;
    }


    public String getmTitle() {
        return mTitle;
    }


    public String getmSection() {
        return mSection;
    }


    public String getmAuthor() {
        return mAuthor;
    }


    public String getmDate() {
        return mDate;
    }


    public String getmUrl() {
        return mUrl;
    }

}
