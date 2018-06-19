package com.example.jaminhu.newsappstage2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsItem> {
    public NewsAdapter(@NonNull Context context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentView = convertView;
        if (currentView == null){
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView titleView = currentView.findViewById(R.id.title_view);
        titleView.setText(getItem(position).getTitle());

        TextView categoryView = currentView.findViewById(R.id.category_view);
        categoryView.setText(getItem(position).getCategory());

        TextView dateView = currentView.findViewById(R.id.date_view);
        dateView.setText(getItem(position).getDate());

        TextView authorView = currentView.findViewById(R.id.author_view);
        authorView.setText(getItem(position).getAuthor());

        return currentView;
    }
}
