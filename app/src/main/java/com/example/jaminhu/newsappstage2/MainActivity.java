package com.example.jaminhu.newsappstage2;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsItem>> {

    private String urlString = "https://content.guardianapis.com/search?api-key=test&format=json&show-tags=contributor";

    private NewsAdapter listViewAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = findViewById(R.id.listView);

        mEmptyStateTextView = findViewById(R.id.status_view);
        listview.setEmptyView(mEmptyStateTextView);

        listViewAdapter = new NewsAdapter(this, new ArrayList<NewsItem>());

        listview.setAdapter(listViewAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem currentNewsItem = listViewAdapter.getItem(position);

                Uri webpage = Uri.parse(currentNewsItem.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

                startActivity(webIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);

        } else {
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<ArrayList<NewsItem>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //^now this sharedPrefs is an object that pretty much shows all the preferences and what they're
        //currently set as?

        String articlesLoaded = sharedPrefs.getString(
                getString(R.string.settings_articles_loaded_key),
                getString(R.string.settings_articles_loaded_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(urlString);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        //Ok so the baseUri.buildUpon() returns a Uri.Builder Object? yes probably

        uriBuilder.appendQueryParameter("page-size", articlesLoaded);
        uriBuilder.appendQueryParameter("order-by", orderBy);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsItem>> loader, ArrayList<NewsItem> data) {
        listViewAdapter.clear();

        View progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_news_found);

        if (data != null && !data.isEmpty()) {
            listViewAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsItem>> loader) {
        listViewAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            Log.d("myTag", "Options clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
        //^ok wait, what is going on here :D what does it mean to return a super thing here...
        //especially when it looks like its returning the method itself to itself...?
    }
}
