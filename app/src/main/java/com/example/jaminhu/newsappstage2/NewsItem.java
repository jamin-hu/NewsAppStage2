package com.example.jaminhu.newsappstage2;

public class NewsItem {

    private String mTitle;
    private String mCategory;
    private String mDate;
    private String mAuthor;
    private String mUrl;

    public NewsItem(String title, String category, String date, String author, String url){
        mTitle = title;
        mCategory = category;
        mDate = date;
        mAuthor = author;
        mUrl = url;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getCategory(){
        return mCategory;
    }

    public String getDate(){
        return mDate;
    }

    public String getAuthor() { return mAuthor; }

    public String getUrl(){
        return mUrl;
    }

}
