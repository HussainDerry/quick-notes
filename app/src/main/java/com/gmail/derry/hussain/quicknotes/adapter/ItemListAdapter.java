/*
 * Copyright (c) 2016 Hussain Al-Derry.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmail.derry.hussain.quicknotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gmail.derry.hussain.quicknotes.R;
import com.gmail.derry.hussain.quicknotes.model.Note;

import java.util.List;

public class ItemListAdapter extends BaseAdapter {

    private static List<Note> mNotesList;
    private LayoutInflater mLayoutInflater;

    public ItemListAdapter(Context context, List<Note> results) {
        mNotesList = results;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mNotesList.size();
    }

    public Note getItem(int position) {
        return mNotesList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        NoteViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item, null);
            holder = new NoteViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(R.id.title);
            holder.mText = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);

        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }

        holder.mTitle.setText(mNotesList.get(position).getTitle());
        holder.mText.setText(mNotesList.get(position).getBody());

        return convertView;
    }

    static class NoteViewHolder {
        TextView mTitle;
        TextView mText;
    }
}
