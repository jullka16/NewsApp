package com.example.android.newsapp;

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

    private static String makeHttpRequest(URL url) throws IOException{
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
                Log.e(LOG_TAG, "Error response code: "+urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retriving the JSON results.", e);
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

    private static ArrayList<Article> extractArticles(String articleJson){
        ArrayList<Article> articles = new ArrayList<>();
        String title;
        String author ="unknown";
        String section;
        String date;
        String url;

        try {
            JSONObject jsonRootObject = new JSONObject(articleJson);
            JSONObject jsonObject = jsonRootObject.optJSONObject("response");
            JSONArray jsonArray = jsonObject.optJSONArray("results");

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject currJsonObject = jsonArray.getJSONObject(i);
                title = currJsonObject.getString("webTitle");
                String temp;
                Article currArticle;
                section = currJsonObject.getString("sectionName");
                date = currJsonObject.getString("webPublicationDate");

                if(currJsonObject.has("webTitle")){
                    JSONArray weirdArray = currJsonObject.optJSONArray("tags");
                    if(weirdArray.length()>0) {
                        JSONObject weirdObject = weirdArray.getJSONObject(0);
                        author = weirdObject.getString("webTitle");
                    }
                }

                url = currJsonObject.getString("webUrl");
                Log.e("QueryUtils", title+section+date);
                currArticle = new Article(title, section,author, date, url);
                articles.add(currArticle);
            }



        } catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return articles;
    }

    public static List<Article> fetchArticleData (String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Article> articles = extractArticles(jsonResponse);
        return  articles;
    }
}

