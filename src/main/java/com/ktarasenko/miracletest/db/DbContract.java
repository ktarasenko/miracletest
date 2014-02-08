package com.ktarasenko.miracletest.db;

import android.provider.BaseColumns;

final class DbContract {

    private DbContract() {}

    public static abstract class EntriesTable implements BaseColumns {
        public static final String TABLE_NAME = "entries";
        public static final String COLUMN_NAME_ID = "entry_id";
        public static final String COLUMN_NAME_TEXT = "entry_text";
        public static final String COLUMN_NAME_COMPLETED = "entry_completed";
        public static final String COLUMN_NAME_NEXT = "entry_next";
        public static final String COLUMN_NAME_PREV = "entry_prev";
        public static final String COLUMN_NAME_ORDER = "entry_order";
    }
}