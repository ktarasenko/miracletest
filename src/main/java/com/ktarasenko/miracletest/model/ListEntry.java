package com.ktarasenko.miracletest.model;

public class ListEntry {

    private final String mId;
    private boolean mCompleted;
    private String mText;

    public ListEntry(String id, String text, boolean isCompleted){
        mId = id;
        mText = text;
        mCompleted = isCompleted;
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

    @Override
    public String toString() {
        return mText;
    }

    public void setCompleted(boolean isCompleted) {
        mCompleted = isCompleted;
    }
}
