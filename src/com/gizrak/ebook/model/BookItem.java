
package com.gizrak.ebook.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class BookItem extends BaseItem {
    private static final String TAG = "BookItem";

    private Type mType;
    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private String mIsbn;
    private String mLanguage;
    private String mCoverPath;
    private byte[] mCover;
    private String mPath;

    private String mTocFilePath;

    private List<ChapterItem> mToc;

    public enum Type {
        EPUB(0),
        TXT(1);

        private int value;

        private Type(int value) {
            this.value = value;
        }

        public int toInteger() {
            return value;
        }

        public static Type valueOf(int value) {
            if (value == EPUB.value) {
                return EPUB;
            } else if (value == TXT.value) {
                return TXT;
            }
            return null;
        }
    }

    public BookItem() {
        super(TAG, null);
    }

    public BookItem(String id) {
        super(TAG, id);
    }

    public Type getType() {
        return mType;
    }

    public void setType(int type) {
        mType = Type.valueOf(type);
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

    public String getCoverFilePath() {
        return mCoverPath;
    }

    public void setCoverFilePath(String path) {
        mCoverPath = path;
    }

    public byte[] getCover() {
        return mCover;
    }

    public void setCover(byte[] cover) {
        mCover = cover;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getTocFilePath() {
        return mTocFilePath;
    }

    public void setTocFilePath(String path) {
        mTocFilePath = path;
    }

    public List<ChapterItem> getToc() {
        return mToc;
    }

    public void setToc(ArrayList<ChapterItem> list) {
        mToc = list;
    }

    private BookItem(Parcel src) {
        super(src);
        mType = Type.valueOf(src.readInt());
        mTitle = src.readString();
        mAuthor = src.readString();
        mPublisher = src.readString();
        mIsbn = src.readString();
        mLanguage = src.readString();
        mCover = new byte[src.readInt()];
        src.readByteArray(mCover);
        mPath = src.readString();
        mToc = new ArrayList<ChapterItem>();
        src.readTypedList(mToc, ChapterItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mType.toInteger());
        dest.writeString(mTitle);
        dest.writeString(mAuthor);
        dest.writeString(mPublisher);
        dest.writeString(mIsbn);
        dest.writeString(mLanguage);
        dest.writeInt(mCover.length);
        dest.writeByteArray(mCover);
        dest.writeString(mPath);
        dest.writeTypedList(mToc);
        // dest.writeParcelableArray(mToc.toArray(new ChapterItem[mToc.size()]),
        // flags);
    }

    public static final Parcelable.Creator<BookItem> CREATOR = new Parcelable.Creator<BookItem>() {

        public BookItem createFromParcel(Parcel in) {
            return new BookItem(in);
        }

        public BookItem[] newArray(int size) {
            return new BookItem[size];
        }
    };
}
