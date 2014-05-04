
package com.gizrak.ebook.model;

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
}
