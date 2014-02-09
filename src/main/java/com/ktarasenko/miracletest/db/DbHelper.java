package com.ktarasenko.miracletest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.ktarasenko.miracletest.db.DbContract.EntriesTable;
import com.ktarasenko.miracletest.model.ListEntry;
import com.ktarasenko.miracletest.utils.Logger;
import com.ktarasenko.miracletest.utils.Utils;

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

    private final String mDeviceId;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDeviceId = Utils.getUniqueID(context);
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


    public Cursor getEntriesCursor(){
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();


            String[] entriesProjection = {
                    EntriesTable._ID,
                    EntriesTable.COLUMN_NAME_ID,
                    EntriesTable.COLUMN_NAME_TEXT,
                    EntriesTable.COLUMN_NAME_COMPLETED,
                    EntriesTable.COLUMN_NAME_ORDER,
                    EntriesTable.COLUMN_NAME_PREV,
                    EntriesTable.COLUMN_NAME_NEXT
            };
            Cursor cursor = db.query(EntriesTable.TABLE_NAME,
                    entriesProjection, null, null, null, null, EntriesTable.COLUMN_NAME_ORDER + " DESC");

            return cursor;

        } catch (SQLException ex){
            Logger.error(TAG, ex.getMessage(), ex);
            if (db != null){
                db.close();
            }
        }
        return null;
    }

    public Cursor getEntriesCursorUnordered(){
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();


            String[] entriesProjection = {
                    EntriesTable._ID,
                    EntriesTable.COLUMN_NAME_ID,
                    EntriesTable.COLUMN_NAME_TEXT,
                    EntriesTable.COLUMN_NAME_COMPLETED,
                    EntriesTable.COLUMN_NAME_ORDER,
                    EntriesTable.COLUMN_NAME_PREV,
                    EntriesTable.COLUMN_NAME_NEXT
            };
            Cursor cursor = db.query(EntriesTable.TABLE_NAME,
                    entriesProjection, null, null, null, null, null);

            return cursor;

        } catch (SQLException ex){
            Logger.error(TAG, ex.getMessage(), ex);
            if (db != null){
                db.close();
            }
        }
        return null;
    }

    public long addNewEntry(String text) {
        SQLiteDatabase db = null;
        long rowId = 0;

        try {
            db = getWritableDatabase();

            ContentValues values = new ContentValues();

            db.beginTransaction();
            Cursor c= db.query(EntriesTable.TABLE_NAME,
                    new String [] {"MAX("+EntriesTable.COLUMN_NAME_ORDER+")",
                            EntriesTable.COLUMN_NAME_ID},
                    null, null, null, null, null);
            c.moveToFirst();

            Integer lastOrder = c.getInt(0)+1;
            String currId =  mDeviceId + lastOrder;
            String nextId = c.getString(1);

            values.put(EntriesTable.COLUMN_NAME_ID, currId);
            values.put(EntriesTable.COLUMN_NAME_TEXT, text);
            values.put(EntriesTable.COLUMN_NAME_ORDER, lastOrder);
            values.put(EntriesTable.COLUMN_NAME_NEXT, nextId);
            values.put(EntriesTable.COLUMN_NAME_COMPLETED, 0);

            rowId = db.insert(
                    EntriesTable.TABLE_NAME,
                    null,
                    values);

            values = new ContentValues();
            values.put(EntriesTable.COLUMN_NAME_PREV, currId);
            db.update(
                    EntriesTable.TABLE_NAME,
                    values,
                    EntriesTable.COLUMN_NAME_ID + " = ?",
                    new String[]{nextId});

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

    public void swapElements(ListEntry entry1, ListEntry entry2) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            db.beginTransaction();
            updateLinks(db, entry1, entry2);
            updateLinks(db, entry2, entry1);

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

    private void updateLinks(SQLiteDatabase db, ListEntry entry1, ListEntry entry2) {
        if (entry1.getPrev() != null && !TextUtils.equals(entry1.getPrev(), entry2.getId())){
            ContentValues v = new ContentValues();
            v.put(EntriesTable.COLUMN_NAME_NEXT, entry2.getId());
            db.update(
                    EntriesTable.TABLE_NAME,
                    v,
                    EntriesTable.COLUMN_NAME_ID + " = ?",
                    new String[]{entry1.getPrev()});
        }
        if (entry1.getNext() != null && !TextUtils.equals(entry1.getNext(), entry2.getId())){
            ContentValues v = new ContentValues();
            v.put(EntriesTable.COLUMN_NAME_PREV, entry2.getId());
            db.update(
                    EntriesTable.TABLE_NAME,
                    v,
                    EntriesTable.COLUMN_NAME_ID + " = ?",
                    new String[]{entry1.getNext()});
        }
        ContentValues v = new ContentValues();
        v.put(EntriesTable.COLUMN_NAME_ORDER, entry2.getOrder());
        v.put(EntriesTable.COLUMN_NAME_PREV, TextUtils.equals(entry1.getNext(), entry2.getId())?
                entry2.getId(): entry2.getPrev());
        v.put(EntriesTable.COLUMN_NAME_NEXT, TextUtils.equals(entry1.getPrev(), entry2.getId())?
                entry2.getId() : entry2.getNext());
        db.update(
                EntriesTable.TABLE_NAME,
                v,
                EntriesTable.COLUMN_NAME_ID + " = ?",
                new String[]{entry1.getId()});
    }

    public static ListEntry getItem(Cursor cursor) {
        String id = cursor.getString(
                cursor.getColumnIndexOrThrow(EntriesTable.COLUMN_NAME_ID));
        String text = cursor.getString(
                cursor.getColumnIndexOrThrow(EntriesTable.COLUMN_NAME_TEXT));
        boolean isCompleted = cursor.getInt(
                cursor.getColumnIndexOrThrow(EntriesTable.COLUMN_NAME_COMPLETED)) == 1;
        Integer order = cursor.getInt(
                cursor.getColumnIndexOrThrow(EntriesTable.COLUMN_NAME_ORDER));
        String next = cursor.getString(
                cursor.getColumnIndexOrThrow(EntriesTable.COLUMN_NAME_NEXT));
        String prev = cursor.getString(
                cursor.getColumnIndexOrThrow(EntriesTable.COLUMN_NAME_PREV));
        return new ListEntry(id, text, isCompleted, order, TextUtils.isEmpty(prev)? null : prev, TextUtils.isEmpty(next)? null: next);
    }
}