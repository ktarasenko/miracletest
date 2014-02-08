/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ktarasenko.miracletest.view;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ResourceCursorAdapter;
import com.ktarasenko.miracletest.db.DbHelper;
import com.ktarasenko.miracletest.model.ListEntry;

public class ItemAdapter extends ResourceCursorAdapter {

    public ItemAdapter(Context context, int layout, Cursor c) {
        super(context, layout, c, true);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CheckedTextView tw = (CheckedTextView) view;
        ListEntry entry = DbHelper.getItem(cursor);
        tw.setText(entry.getText());
        tw.setChecked(entry.isCompleted());
    }

}
