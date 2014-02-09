package com.ktarasenko.miracletest.model;

public class ListEntry {

    private final String mId;
    private final boolean mCompleted;
    private final String mText;
    private final int mOrder;
    private final String mPrev;
    private final String mNext;

    public ListEntry(String id, String text, boolean isCompleted, int order, String prev, String next){
        mId = id;
        mText = text;
        mCompleted = isCompleted;
        mOrder = order;
        mPrev = prev;
        mNext = next;
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

    public String getPrev() {
        return mPrev;
    }

    public String getNext() {
        return mNext;
    }

    @Override
    public String toString() {
        return mText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListEntry entry = (ListEntry) o;

        if (mCompleted != entry.mCompleted) return false;
        if (mOrder != entry.mOrder) return false;
        if (!mId.equals(entry.mId)) return false;
        if (mNext != null ? !mNext.equals(entry.mNext) : entry.mNext != null) return false;
        if (mPrev != null ? !mPrev.equals(entry.mPrev) : entry.mPrev != null) return false;
        if (!mText.equals(entry.mText)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + (mCompleted ? 1 : 0);
        result = 31 * result + mText.hashCode();
        result = 31 * result + mOrder;
        result = 31 * result + (mPrev != null ? mPrev.hashCode() : 0);
        result = 31 * result + (mNext != null ? mNext.hashCode() : 0);
        return result;
    }
}
