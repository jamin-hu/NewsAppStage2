package com.example.jaminhu.newsappstage2;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsItem>> {

    private String mUrlString;

    public NewsLoader(Context context, String urlString) {
        super(context);
        mUrlString = urlString;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public ArrayList<NewsItem> loadInBackground() {

        if (mUrlString== null){
            return null;
        }

        ArrayList<NewsItem> listOfNewsItems = UrlToObjectConverter.fetchObjectsFromUrl(mUrlString);
        return listOfNewsItems;
    }
}
