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

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public void setmSection(String mSection) {
        this.mSection = mSection;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
