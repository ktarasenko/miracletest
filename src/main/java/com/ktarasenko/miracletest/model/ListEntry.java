package com.ktarasenko.miracletest.model;

public class ListEntry {

    private final String mId;
    private final boolean mCompleted;
    private final String mText;
    private final int mOrder;

    public ListEntry(String id, String text, boolean isCompleted, int order){
        mId = id;
        mText = text;
        mCompleted = isCompleted;
        mOrder = order;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public String getText() {
        return mText;
    }

    public String getId() {
        return mId;
    }

    public int getOrder() {
        return mOrder;
    }

    @Override
    public String toString() {
        return mText;
    }


}
