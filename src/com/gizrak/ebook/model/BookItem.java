
package com.gizrak.ebook.model;

import android.graphics.Bitmap;
import android.os.Parcel;

public class BookItem extends BaseItem {
    private static final String TAG = "BookItem";

    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private String mIsbn;
    private String mLanguage;
    private Bitmap mCover;
    private String mPath;

    public BookItem(String id) {
        super(TAG, id);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(String publisher) {
        mPublisher = publisher;
    }

    public String getIsbn() {
        return mIsbn;
    }

    public void setIsbn(String isbn) {
        mIsbn = isbn;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public Bitmap getCover() {
        return mCover;
    }

    public void setCover(Bitmap cover) {
        mCover = cover;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    private BookItem(Parcel source) {
        super(source);

    }
}
