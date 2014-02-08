package com.ktarasenko.miracletest.model;

public class ListEntry {

    private final String mId;
    private boolean mChecked;
    private String mText;

    public ListEntry(String id, String text){
        mId = id;
        mText = text;
    }

    public boolean isChecked() {
        return mChecked;
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

    public void setChecked(boolean isChecked) {
        mChecked = isChecked;
    }
}
