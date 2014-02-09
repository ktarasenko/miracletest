package com.ktarasenko.miracletest.test.model;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.ktarasenko.miracletest.db.DbHelper;
import com.ktarasenko.miracletest.model.ListEntry;
import com.ktarasenko.miracletest.test.data.Cheeses;

import java.util.*;

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
        String[] texts = Cheeses.sCheeseStrings;
        ArrayList<ListEntry> newEntries = readDb();
        assertEquals("elements cound in db and in stub is the same", texts.length, newEntries.size());
        for (int i = 0; i < texts.length; i++){
            ListEntry newEntry = newEntries.get(i);
            String text = texts[i];
            assertEquals("Text should be equals", text, newEntry.getText());
        }
    }


    public void testListReconstruction(){
        fillDb();
        ArrayList<ListEntry> entries = readDb();
        ArrayList<ListEntry> entriesFromList = readDbAsList();
        assertEquals("Lists sorted by order and list reconstructed using links should be equals", entries, entriesFromList);
    }

    public void testSwapElements(){
        fillDb();
        int count = Cheeses.sCheeseStrings.length;
        Random r = new Random();
        //some edge cases
        subtestSwapElements(0, 0);
        subtestSwapElements(0, count-1);
        subtestSwapElements(0, count-2);
        subtestSwapElements(1, 2);
        subtestSwapElements(2, 1);
        //some random permutations
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
        ArrayList<ListEntry> entriesNewFromList = readDbAsList();
        ListEntry e01 = entriesNew.get(index1);
        ListEntry e02= entriesNew.get(index2);
        assertEquals("list order should be the same after swap " + index1 + " " + index2, entriesNew, entriesNewFromList);
        assertTrue("Entries should be swapped",
                e1.getId().equals(e02.getId())
                        && e1.getText().equals(e02.getText())
                        && e01.getId().equals(e2.getId())
                        && e01.getText().equals(e2.getText()));
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

    private ArrayList<ListEntry> readDbAsList() {
        HashMap<String, ListEntry> entries = new HashMap<String, ListEntry>();
        Cursor cursor = mDbHelper.getEntriesCursorUnordered();
        cursor.moveToFirst();
        String firstId = null;
        while (!cursor.isAfterLast()){
            ListEntry entry = DbHelper.getItem(cursor);
            entries.put(entry.getId(), entry);
            if (entry.getPrev() == null){
                assertNull("there's should be only one element with prev= null", firstId);
                firstId = entry.getId();
            }
            cursor.moveToNext();
        }
        cursor.close();
        ArrayList<ListEntry> list = new ArrayList<ListEntry>(entries.size());
        String entryId = firstId;
        while (entryId != null){
            ListEntry entry = entries.get(entryId);
            assertNotNull("List shouldn't have duplicated ids. Probably linked list is broken",entry);
            list.add(entry);
            entries.remove(entryId);
            entryId = entry.getNext();
        }
        assertTrue("All elements should be used in list", entries.isEmpty());
        return list;
    }



    private void fillDb() {
        ArrayList<String> list= new ArrayList<String>();
        list.addAll(Arrays.asList(Cheeses.sCheeseStrings));

        Collections.reverse(list);
        for (String text : list){
            mDbHelper.addNewEntry(text);
        }
    }

}

