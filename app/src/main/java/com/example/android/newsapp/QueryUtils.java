package com.example.android.newsapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia on 2018-03-17.
 */

public final class QueryUtils {
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String makeHttpRequest(URL url, Context context) throws IOException{
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else{
                Log.e(LOG_TAG, context.getString(R.string.error) +urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, context.getString(R.string.problem_json), e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static ArrayList<Article> extractArticles(String articleJson, Context context){
        ArrayList<Article> articles = new ArrayList<>();
        String title;
        String author = context.getString(R.string.unknown);
        String section;
        String date;
        String url;

        try {
            JSONObject jsonRootObject = new JSONObject(articleJson);
            JSONObject jsonObject = jsonRootObject.optJSONObject(context.getString(R.string.response));
            JSONArray jsonArray = jsonObject.optJSONArray(context.getString(R.string.results));

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject currJsonObject = jsonArray.getJSONObject(i);
                title = currJsonObject.getString(context.getString(R.string.web_title));
                String temp;
                Article currArticle;
                section = currJsonObject.getString(context.getString(R.string.section_name));
                date = currJsonObject.getString(context.getString(R.string.pub_date));

                if(currJsonObject.has(context.getString(R.string.web_title))){
                    JSONArray weirdArray = currJsonObject.optJSONArray(context.getString(R.string.tags));
                    if(weirdArray.length()>0) {
                        JSONObject weirdObject = weirdArray.getJSONObject(0);
                        author = weirdObject.getString(context.getString(R.string.web_title));
                    }
                }

                url = currJsonObject.getString(context.getString(R.string.web_url));
                Log.e(context.getString(R.string.query_utils), title+section+date);
                currArticle = new Article(title, section,author, date, url);
                articles.add(currArticle);
            }



        } catch (JSONException e){
            Log.e(context.getString(R.string.query_utils), context.getString(R.string.problem_parsing), e);
        }

        return articles;
    }

    public static List<Article> fetchArticleData (String requestUrl, Context context){
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url, context);
        } catch (IOException e){
            Log.e(LOG_TAG, context.getString(R.string.problem_request));
        }

        List<Article> articles = extractArticles(jsonResponse, context);
        return  articles;
    }
}

