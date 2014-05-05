
package com.gizrak.ebook.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChapterItem extends BaseItem {
    private static final String TAG = "ChapterItem";

    private int mOrder;
    private String mLabel;
    private String mContent;

    public ChapterItem(String id) {
        super(TAG, id);
    }

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
        mOrder = order;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public ChapterItem(Parcel src) {
        super(src);
        mOrder = src.readInt();
        mLabel = src.readString();
        mContent = src.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mOrder);
        dest.writeString(mLabel);
        dest.writeString(mContent);
    }

    public static final Parcelable.Creator<ChapterItem> CREATOR = new Parcelable.Creator<ChapterItem>() {

        public ChapterItem createFromParcel(Parcel in) {
            return new ChapterItem(in);
        }

        public ChapterItem[] newArray(int size) {
            return new ChapterItem[size];
        }
    };
}
