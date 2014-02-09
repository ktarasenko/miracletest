package com.ktarasenko.miracletest.test.model;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.ktarasenko.miracletest.db.DbHelper;

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
    }

}

