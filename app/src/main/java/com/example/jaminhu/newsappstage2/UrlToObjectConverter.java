package com.example.jaminhu.newsappstage2;

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

public final class UrlToObjectConverter {

    private static final String LOG_TAG = UrlToObjectConverter.class.getSimpleName();

    private UrlToObjectConverter() {
    }

    public static ArrayList<NewsItem> fetchObjectsFromUrl (String url){

        String jsonResponse = null;
        try {
            jsonResponse = fetchJsonResponse(convertUrl(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<NewsItem> newsItems = parseJsonResponse(jsonResponse);

        return newsItems;
    }

    private static String fetchJsonResponse(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<NewsItem> parseJsonResponse(String jsonResponse){

        ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();

        try {
            JSONObject objectJsonResponse = new JSONObject(jsonResponse);
            JSONObject response = objectJsonResponse.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++ ){
                JSONObject news = results.getJSONObject(i);
                String name = news.getString("webTitle");
                String category = news.getString("sectionName");
                String date = news.getString("webPublicationDate");

                JSONArray tags = news.getJSONArray("tags");
                String author;
                if (tags.length() != 0 && tags != null){
                    JSONObject firstAuthor = tags.getJSONObject(0);
                    author = firstAuthor.getString("webTitle");
                } else {
                    author = "Anonymous";
                };

                String newsUrl = news.getString("webUrl");

                NewsItem newsItem = new NewsItem(name, category, date, author, newsUrl);
                newsItems.add(newsItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsItems;
    }

    private static URL convertUrl(String stringUrl){
        URL usableUrl = null;
        try {
            usableUrl = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return usableUrl;
    }
}
