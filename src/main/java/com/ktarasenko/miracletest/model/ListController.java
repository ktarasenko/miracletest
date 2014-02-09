package com.ktarasenko.miracletest.model;

import android.content.Context;
import android.database.Cursor;
import android.widget.BaseAdapter;
import com.ktarasenko.miracletest.R;
import com.ktarasenko.miracletest.db.DbHelper;
import com.ktarasenko.miracletest.utils.Logger;
import com.ktarasenko.miracletest.utils.Utils;
import com.ktarasenko.miracletest.view.ItemAdapter;

import java.util.ArrayList;

/**
 * Thick controller that updates db and current adapter
 */
public class ListController {

    private final DbHelper mDbHelper;
    private final Context mContext;
    private final ItemAdapter mAdapter;

    public ListController(Context context){
        mContext = context;
        mDbHelper = new DbHelper(context);

        mAdapter =  new ItemAdapter(mContext, R.layout.text_view, mDbHelper.getEntriesCursor());
    }

    public BaseAdapter getAdapter(){
       return mAdapter;
    }

    public void addEntry(String text){
        mDbHelper.addNewEntry(text);
        requery();
    }

    private void requery() {
        mAdapter.changeCursor(mDbHelper.getEntriesCursor());
    }

    public void toggleCompleted(int index){
        ListEntry entry = DbHelper.getItem((Cursor) mAdapter.getItem(index));
        mDbHelper.updateCompleted(entry.getId(), !entry.isCompleted());
        requery();
    }

    public void swapElements(int indexOne, int indexTwo) {
        mDbHelper.swapElements(
                DbHelper.getItem((Cursor) mAdapter.getItem(indexOne)),
                DbHelper.getItem((Cursor) mAdapter.getItem(indexTwo)));
        requery();
    }
}
