package com.ktarasenko.miracletest.model;

import android.content.Context;
import android.widget.BaseAdapter;
import com.ktarasenko.miracletest.R;
import com.ktarasenko.miracletest.db.DbHelper;
import com.ktarasenko.miracletest.utils.Utils;
import com.ktarasenko.miracletest.view.StableArrayAdapter;

import java.util.ArrayList;

/**
 * Thick controller that updates db and current adapter
 */
public class ListController {

    private final ArrayList<ListEntry> mList = new ArrayList<ListEntry>();
    private final DbHelper mDbHelper;
    private final Context mContext;
    private final String mDeviceId;
    private final StableArrayAdapter mAdapter;

    public ListController(Context context){
        mContext = context;
        mDeviceId = Utils.getUniqueID(mContext);
        mDbHelper = new DbHelper(context);
        mAdapter =  new StableArrayAdapter(mContext, R.layout.text_view, mList);
        mList.addAll(mDbHelper.getEntries());
    }

    public BaseAdapter getAdapter(){
       return mAdapter;
    }

    public void addEntry(String text){
        ListEntry entry = new ListEntry(mDeviceId + mList.size(), text.toString(), false);
        mList.add(0, entry);
        mDbHelper.addEntry(0, entry);
        mAdapter.notifyDataSetChanged();
    }

    public void toggleCompleted(int index){
        toggleCompleted(mList.get(index));
    }

    public void toggleCompleted(ListEntry entry){
       entry.setCompleted(!entry.isCompleted());
       mDbHelper.updateCompleted(entry.getId(), entry.isCompleted());
       mAdapter.notifyDataSetChanged();
    }

    public void swapElements(int indexOne, int indexTwo) {
        mDbHelper.swapElements(mList.get(indexOne).getId(), mList.get(indexTwo).getId(), indexOne, indexTwo);
        ListEntry temp = mList.get(indexOne);
        mList.set(indexOne, mList.get(indexTwo));
        mList.set(indexTwo, temp);
        mAdapter.notifyDataSetChanged();
    }
}
