package com.ktarasenko.miracletest.test.model;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.ktarasenko.miracletest.db.DbHelper;
import com.ktarasenko.miracletest.model.ListEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ModelTest extends AndroidTestCase {

    private static final String TEST_FILE_PREFIX = "test_";
    DbHelper mDbHelper;


    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context
                = new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);

        mDbHelper = new DbHelper(context);
    }

    public void testPreConditions() {
        assertNotNull(mDbHelper);
        assertTrue("Database is empty", mDbHelper.getEntriesCursor().getCount() == 0);
    }


    public void testAddItems(){
        fillDb();
        ArrayList<ListEntry> entries = createEntriesStub();
        ArrayList<ListEntry> newEntries = readDb();
        assertEquals("elements cound in db and in stub is the same", entries.size(), newEntries.size());
        for (int i = 0; i < entries.size(); i++){
            ListEntry newEntry = newEntries.get(i);
            ListEntry entry = entries.get(i);
            assertEquals("Text should be equals", entry.getText(), newEntry.getText());
            assertEquals("Order should be equals", entry.getOrder(), newEntry.getOrder());
        }
    }


    public void testCompleted(){
        fillDb();
        Cursor cursor = mDbHelper.getEntriesCursor();
        cursor.moveToFirst();
        assertTrue("Database shouldn't be empty", cursor.getCount()> 0);
        while (!cursor.isAfterLast()){
            ListEntry entry = DbHelper.getItem(cursor);
            if (entry.getOrder() % 2 == 0){
              mDbHelper.updateCompleted(entry.getId(), true);
            }
            cursor.moveToNext();
        }
        cursor.close();
        cursor = mDbHelper.getEntriesCursor();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            ListEntry entry = DbHelper.getItem(cursor);
            assertEquals("Elements with even order should be completed", entry.getOrder() % 2 == 0, entry.isCompleted());
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void testSwapElements(){
        fillDb();
        int count = createEntriesStub().size();
        Random r = new Random();
        for(int i = 0; i < 100; i++){
            subtestSwapElements(r.nextInt(count), r.nextInt(count));
        }
    }

    private void subtestSwapElements(int index1, int index2){
        ArrayList<ListEntry> entries = readDb();
        ListEntry e1 = entries.get(index1);
        ListEntry e2 = entries.get(index2);
        mDbHelper.swapElements(e1, e2);
        ArrayList<ListEntry> entriesNew = readDb();
        ListEntry e01 = entriesNew.get(index1);
        ListEntry e02= entriesNew.get(index2);
        assertTrue("Entries should be swapped",
                e1.getId().equals(e02.getId())
                        && e1.getText().equals(e02.getText())
                        && e01.getId().equals(e2.getId())
                        && e01.getText().equals(e2.getText()));
    }

    private ArrayList<ListEntry> readDb() {
        ArrayList<ListEntry> entries = new ArrayList<ListEntry>();
        Cursor cursor = mDbHelper.getEntriesCursor();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            entries.add(DbHelper.getItem(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }


    private void fillDb() {
        ArrayList<ListEntry> entries = createEntriesStub();
        Collections.reverse(entries);
        for (ListEntry entry : entries){
            mDbHelper.addNewEntry(entry.getText());
        }
    }

    private ArrayList<ListEntry> createEntriesStub(){
        ArrayList<ListEntry> entries = new ArrayList<ListEntry>();
        entries.add(new ListEntry("6", "six", false, 6));
        entries.add(new ListEntry("5", "five", false, 5));
        entries.add(new ListEntry("4", "four", false, 4));
        entries.add(new ListEntry("3", "three", false, 3));
        entries.add(new ListEntry("2", "two", false, 2));
        entries.add(new ListEntry("1", "one", false, 1));
        return entries;
    }

}

