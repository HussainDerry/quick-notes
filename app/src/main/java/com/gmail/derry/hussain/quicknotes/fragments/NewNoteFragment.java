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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.derry.hussain.quicknotes.R;
import com.gmail.derry.hussain.quicknotes.activities.MainActivity;
import com.gmail.derry.hussain.quicknotes.core.AppCore;
import com.gmail.derry.hussain.quicknotes.database.NoteDao;
import com.gmail.derry.hussain.quicknotes.model.Note;
import com.gmail.derry.hussain.utils.DateTimeUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewNoteFragment extends Fragment {

    @BindView(R.id.button_save) Button button_save;
    @BindView(R.id.button_clear) Button button_clear;
    @BindView(R.id.editText_title) EditText mTitle;
    @BindView(R.id.editText_text) EditText mText;
    @BindView(R.id.ad_view) AdView mAdView;

    public NewNoteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_new_note, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadBannerAd();
    }

    @OnClick(R.id.button_save)
    public void clickSave(){
        addNote(mTitle.getText().toString(), mText.getText().toString());
    }

    @OnClick(R.id.button_clear)
    public void clickClear(){
        mTitle.setText("");
        mText.setText("");
    }

    private void addNote(String title, String content) {
        NoteDao mNoteDao = AppCore.getInstance().getDaoSession().getNoteDao();
        Note mNote = new Note();

        if (!mText.getText().toString().equals("")) {

            mNote.setTitle(title);
            mNote.setBody(content);
            mNote.setDate(DateTimeUtils.currentDateAsString());

            mNoteDao.insert(mNote);
            Toast.makeText(getActivity(), "Note Added Successfully", Toast.LENGTH_LONG).show();
            ((MainActivity) getActivity()).openDrawer();
        } else {
            Toast.makeText(getActivity(), "Note Cannot Be Empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBannerAd(){
        AdRequest mRequest = new AdRequest.Builder().addTestDevice("Your Device Id Here!").build();
        mAdView.loadAd(mRequest);
    }
}
