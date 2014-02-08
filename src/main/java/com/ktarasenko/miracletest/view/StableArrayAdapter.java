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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;
import com.ktarasenko.miracletest.R;
import com.ktarasenko.miracletest.model.ListEntry;

import java.util.HashMap;
import java.util.List;

public class StableArrayAdapter extends EntityListAdapter<ListEntry> {

    final int INVALID_ID = -1;

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int layoutId, List<ListEntry> objects) {
        super(context, layoutId, objects, new ItemHolderCreator(context));
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i).getId(), i);
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        String id = getItem(position).getId();
        return mIdMap.get(id);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private static class ItemHolderCreator implements EntityHolderCreator<ListEntry> {
        public ItemHolderCreator(Context context) {

        }

        @Override
        public EntityHolder<ListEntry> createHolder(View convertView) {
            return new ItemHolder(convertView);
        }
    }

    private static class ItemHolder implements EntityHolder<ListEntry> {

        private final CheckedTextView mTextView;

        public ItemHolder(View convertView) {
           mTextView = (CheckedTextView) convertView;
        }


        @Override
        public void update(ListEntry object) {
            mTextView.setText(object.getText());
            mTextView.setChecked(object.isChecked());
//            mTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mTextView.toggle();
//                }
//            });
        }
    }
}
