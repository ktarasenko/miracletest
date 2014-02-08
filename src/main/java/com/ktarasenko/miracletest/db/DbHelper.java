package com.ktarasenko.miracletest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ktarasenko.miracletest.db.DbContract.EntriesTable;
import com.ktarasenko.miracletest.model.ListEntry;
import com.ktarasenko.miracletest.utils.Logger;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "Database";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Items.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES_TABLE =
            "CREATE TABLE " + EntriesTable.TABLE_NAME + " (" +
                    EntriesTable._ID + " "+ INT_TYPE + " PRIMARY KEY," +
                    EntriesTable.COLUMN_NAME_ID+ TEXT_TYPE + COMMA_SEP +
                    EntriesTable.COLUMN_NAME_NEXT+ TEXT_TYPE + COMMA_SEP +
                    EntriesTable.COLUMN_NAME_PREV+ TEXT_TYPE + COMMA_SEP +
                    EntriesTable.COLUMN_NAME_ORDER+ INT_TYPE + COMMA_SEP +
                    EntriesTable.COLUMN_NAME_TEXT+ TEXT_TYPE + COMMA_SEP +
                    EntriesTable.COLUMN_NAME_COMPLETED+ INT_TYPE +
            " )";


    private static final String SQL_DELETE_ENTRIES_TABLE =
            "DROP TABLE IF EXISTS " + EntriesTable.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       recreateDb(db);
    }

    private SQLiteDatabase recreateDb(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES_TABLE);
        onCreate(db);
        return db;
    }


    public ArrayList<ListEntry> getEntries(){
        SQLiteDatabase db = null;
        ArrayList<ListEntry> entries = new ArrayList<ListEntry>();
        try {
            db = getReadableDatabase();


            String[] entriesProjection = {
                    EntriesTable._ID,
                    EntriesTable.COLUMN_NAME_ID,
                    EntriesTable.COLUMN_NAME_TEXT,
                    EntriesTable.COLUMN_NAME_COMPLETED
            };
            Cursor cursor = db.query(EntriesTable.TABLE_NAME,
                    entriesProjection, null, null, null, null, EntriesTable.COLUMN_NAME_ORDER + " DESC");

            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String id = cursor.getString(
                            cursor.getColumnIndexOrThrow(EntriesTable.COLUMN_NAME_ID));
                    String text = cursor.getString(
                            cursor.getColumnIndexOrThrow(EntriesTable.COLUMN_NAME_TEXT));
                    boolean isCompleted = cursor.getInt(
                            cursor.getColumnIndexOrThrow(EntriesTable.COLUMN_NAME_COMPLETED)) == 1;
                    entries.add(new ListEntry(id, text, isCompleted));
                    cursor.moveToNext();
                }
            }

        } catch (SQLException ex){
            Logger.error(TAG, ex.getMessage(), ex);
        } finally {
            if (db != null){
                db.close();
            }
        }

        return entries;
    }

    public long addEntry(int position, ListEntry entry) {
        SQLiteDatabase db = null;
        long rowId = 0;

        try {
            db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(EntriesTable.COLUMN_NAME_ID, entry.getId());
            values.put(EntriesTable.COLUMN_NAME_TEXT, entry.getText());
            values.put(EntriesTable.COLUMN_NAME_ORDER, position);
            values.put(EntriesTable.COLUMN_NAME_COMPLETED, entry.isCompleted()? 1 : 0);
            db.beginTransaction();

            rowId = db.insert(
                    EntriesTable.TABLE_NAME,
                    null,
                    values);

            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (SQLException ex){
            Logger.error(TAG, ex.getMessage(), ex);
        } finally {
            if (db != null){
                db.close();
            }
        }
        return rowId;
    }

    public void updateCompleted(String id, boolean isCompleted) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EntriesTable.COLUMN_NAME_COMPLETED, isCompleted ? 1 : 0);
            db.update(
                    EntriesTable.TABLE_NAME,
                    values,
                    EntriesTable.COLUMN_NAME_ID + " = ?",
                    new String[]{id});

        } catch (SQLException ex){
            Logger.error(TAG, ex.getMessage(), ex);
        } finally {
            if (db != null){
                db.close();
            }
        }
    }

    public void swapElements(String id1, String id2, int order1, int order2) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            ContentValues valuesOne = new ContentValues();
            valuesOne.put(EntriesTable.COLUMN_NAME_ORDER, order2);

            ContentValues valuesTwo = new ContentValues();
            valuesTwo.put(EntriesTable.COLUMN_NAME_ORDER, order1);

            db.beginTransaction();
            db.update(
                    EntriesTable.TABLE_NAME,
                    valuesOne,
                    EntriesTable.COLUMN_NAME_ID + " = ?",
                    new String[]{id2});
            db.update(
                    EntriesTable.TABLE_NAME,
                    valuesTwo,
                    EntriesTable.COLUMN_NAME_ORDER + " = ?",
                    new String[]{id1});

            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (SQLException ex){
            Logger.error(TAG, ex.getMessage(), ex);
        } finally {
            if (db != null){
                db.close();
            }
        }
    }
}