package com.example.android.booklist;

public class Book {
    private String mTitle;
    private String mAuthors;
    private String mThumbnail;
    private String mUrl;

    public Book(String mTitle, String mAuthors, String mThumbnail, String mUrl) {
        this.mTitle = mTitle;
        this.mAuthors = mAuthors;
        this.mThumbnail = mThumbnail;
        this.mUrl = mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getUrl() {
        return mUrl;
    }
}
