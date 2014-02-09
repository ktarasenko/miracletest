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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.ktarasenko.miracletest.model.ListController;
import com.ktarasenko.miracletest.view.DynamicListView;

public class MainActivity extends Activity {

    private static final String STATE_LIST_POSITION = "list_position";
    private static final String STATE_FIRST_ITEM_OFFSET = "first_item_position";
    private static final String STATE_EDIT_FIELD = "edit_field";
    private DynamicListView mListView;
    private EditText mHeader;
    private ListController mListController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        mListController = new ListController(this);
        mListView = (DynamicListView) findViewById(R.id.listview);
        mListView.setController(mListController);
        mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);

        mHeader = new EditText(this);
        mHeader.setHint(R.string.add_item_hint);
        mHeader.setSingleLine();
        mHeader.setMinHeight(getResources().getDimensionPixelSize(R.dimen.list_item_height));
        mHeader.setImeOptions(EditorInfo.IME_ACTION_SEND);
        mHeader.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String text = v.getText().toString();
                    if (!TextUtils.isEmpty(text)){
                        mListController.addEntry(text);
                        v.setText(null);
                        return true;
                    }
                }
                return false;
            }
        });
        mListView.addHeaderView(mHeader);
        mListView.setAdapter(mListController.getAdapter());
        restoreState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreState(savedInstanceState);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            mListView.setSelectionFromTop(savedInstanceState.getInt(STATE_LIST_POSITION, 0), savedInstanceState.getInt(STATE_FIRST_ITEM_OFFSET, 0));
            mHeader.setText(savedInstanceState.getCharSequence(STATE_EDIT_FIELD));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_LIST_POSITION, mListView.getFirstVisiblePosition());
        if (mListView.getChildCount() > 0){
            outState.putInt(STATE_FIRST_ITEM_OFFSET, mListView.getChildAt(0).getTop());
        }
        outState.putCharSequence(STATE_EDIT_FIELD, mHeader.getText());
    }
}
