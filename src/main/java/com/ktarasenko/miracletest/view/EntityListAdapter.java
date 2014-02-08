package com.ktarasenko.miracletest.view;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class EntityListAdapter<T> extends ArrayAdapter<T> {

    protected final LayoutInflater mInflater;
    private final EntityHolderCreator<T> mCreator;
    private boolean isError = false;
    private int layoutId;

    public EntityListAdapter(Context context, int layoutId, EntityHolderCreator<T> creator) {
        super(context, -1);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCreator = creator;
        this.layoutId = layoutId;
    }

    public EntityListAdapter(Context context, int layoutId, List<T> entities, EntityHolderCreator<T> creator) {
        super(context, -1, entities);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCreator = creator;
        this.layoutId = layoutId;
    }

    public void addAll(List<T> items) {
        if (items != null) {
            this.setNotifyOnChange(false);
            for (T item : items) {
                this.add(item);
            }
            this.setNotifyOnChange(true);
            this.notifyDataSetChanged();
        }
    }

    public void fillAll(Collection<T> items) {
        if (items != null) {
            this.setNotifyOnChange(false);
            this.clear();
            for (T item : items) {
                this.add(item);
            }
            this.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EntityHolder<T> holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutId, null);
            holder = mCreator.createHolder(convertView);
            convertView.setTag(holder);
        } else {
            Object tag = convertView.getTag();
            if (tag instanceof EntityHolder){
                holder = (EntityHolder<T>)tag;
            }
            if (holder == null){
                holder = mCreator.createHolder(convertView);
                convertView.setTag(holder);
            }
        }
        if (holder != null) {
            T item = this.getItem(position);
            holder.update(item);
        }
        return convertView;
    }

    public static interface EntityHolderCreator<T> {
        public EntityHolder<T> createHolder(View convertView);
    }

    public static interface EntityHolder<T> {

        public void update(T object);

    }

}