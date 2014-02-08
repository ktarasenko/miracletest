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

package com.ktarasenko.miracletest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.ktarasenko.miracletest.model.ListEntry;
import com.ktarasenko.miracletest.utils.Utils;
import com.ktarasenko.miracletest.view.Cheeses;
import com.ktarasenko.miracletest.view.DynamicListView;
import com.ktarasenko.miracletest.view.StableArrayAdapter;

import java.util.ArrayList;

/**
 * This application creates a listview where the ordering of the data set
 * can be modified in response to user touch events.
 *
 * An item in the listview is selected via a long press event and is then
 * moved around by tracking and following the movement of the user's finger.
 * When the item is released, it animates to its new position within the listview.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        String idPrefix = Utils.getUniqueID(this);
        ArrayList<ListEntry> cheeseList = new ArrayList<ListEntry>();
        for (int i = 0; i < Cheeses.sCheeseStrings.length; ++i) {

            cheeseList.add(new ListEntry(idPrefix + i, Cheeses.sCheeseStrings[i]));
        }

        StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.text_view, cheeseList);
        DynamicListView listView = (DynamicListView) findViewById(R.id.listview);

        listView.setCheeseList(cheeseList);

        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        TextView tw = new EditText(this);
        listView.addHeaderView(tw);
        listView.setAdapter(adapter);
    }
}
