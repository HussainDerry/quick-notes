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

package com.gmail.derry.hussain.quicknotes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gmail.derry.hussain.quicknotes.R;
import com.gmail.derry.hussain.quicknotes.activities.NoteDetailsActivity;
import com.gmail.derry.hussain.quicknotes.adapter.ItemListAdapter;
import com.gmail.derry.hussain.quicknotes.core.AppCore;
import com.gmail.derry.hussain.quicknotes.database.NoteDao;
import com.gmail.derry.hussain.quicknotes.model.Note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowAllNotesFragment extends Fragment {

    @BindView(R.id.notes_list) ListView mListView;
    private List<Note> mNotes;
	
	public ShowAllNotesFragment(){
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_all_notes, container, false);
        ButterKnife.bind(this, rootView);

        setupListAdapter();
     	setupListItemOnClickListener();

        return rootView;
    }

    private void setupListAdapter(){
        mNotes = getAllNoteFromDatabase();
        mListView.setAdapter(new ItemListAdapter(getActivity(), mNotes));
    }

	/**
	 *  Handle when list item click go to view details activity
	 */
	private void setupListItemOnClickListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
				intent.putExtra("KEY_NOTE", mNotes.get(arg2));
				startActivity(intent);
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}

	/**
	 * Retrieves all notes from the database.
	 * @return {@link List}
	 */
	public List<Note> getAllNoteFromDatabase(){
		NoteDao mNoteDao = AppCore.getInstance().getDaoSession().getNoteDao();
        return mNoteDao.queryBuilder().orderDesc(NoteDao.Properties.Id).list();
	}
}
