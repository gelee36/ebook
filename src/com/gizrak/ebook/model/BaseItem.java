
package com.gizrak.ebook.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class BaseItem implements Parcelable {

    private String mTag;
    private String mId;

    protected BaseItem(String tag, String id) {
        mTag = tag;
        mId = id;
    }

    public String getId() {
        return mId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BaseItem &&
                mId != null &&
                mId.equals(((BaseItem) o).mId);
    }

    @Override
    public String toString() {
        return new StringBuilder(mTag)
                .append("(#")
                .append(mId)
                .append(')')
                .toString();
    }

    protected BaseItem(Parcel src) {
        mTag = src.readString();
        mId = src.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTag);
        dest.writeString(mId);
    }
}
