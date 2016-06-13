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

package com.gmail.derry.hussain.quicknotes.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gmail.derry.hussain.quicknotes.R;
import com.gmail.derry.hussain.quicknotes.core.AppCore;
import com.gmail.derry.hussain.quicknotes.model.Note;
import com.gmail.derry.hussain.quicknotes.utils.GuiUtils;
import com.gmail.derry.hussain.utils.DateTimeUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteDetailsActivity extends AppCompatActivity {

    @BindView(R.id.button_save) Button mButtonUpdate;
    @BindView(R.id.editText_title) EditText mTitle;
    @BindView(R.id.editText_text) EditText mText;
    @BindView(R.id.textView_date) TextView mDate;
    @BindView(R.id.lyt_button) LinearLayout mButtonLayout;
    @BindView(R.id.ad_view) AdView mAdView;
    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        ButterKnife.bind(this);

        setupActionBar();
        loadNoteData();
        disableEditing();
        loadBannerAd();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        new GuiUtils().setColorFromPrefs(getSupportActionBar());
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                break;

            case R.id.action_share:
                methodShare(mTitle.getText().toString(), mText.getText().toString());
                break;

            case R.id.action_edit:
                enableEditing();
                break;

            case R.id.action_delete:
                showDeleteConfirmationDialog();
                break;
        }

        return true;
    }

    @OnClick(R.id.button_save)
    public void clickUpdate() {
        UpdateNote(mNote);
    }

    @OnClick(R.id.button_clear)
    public void clickClear() {
        mTitle.setText("");
        mText.setText("");
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            new GuiUtils().setColorFromPrefs(getSupportActionBar());
        }
    }

    private void loadNoteData() {
        Intent intent = getIntent();
        mNote = (Note) intent.getExtras().getSerializable("KEY_NOTE");
        mButtonUpdate.setText(R.string.button_update);
        mTitle.setText(mNote.getTitle());
        mText.setText(mNote.getBody());
        String date = "Edited : " + mNote.getDate();
        mDate.setText(date);
    }

    private void disableEditing() {
        mButtonLayout.setVisibility(View.GONE);
        mTitle.setEnabled(false);
        mText.setEnabled(false);
    }

    private void enableEditing() {
        mButtonLayout.setVisibility(View.VISIBLE);
        mTitle.setEnabled(true);
        mText.setEnabled(true);
        mText.requestFocus();
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(mText, 0);
        Toast.makeText(getApplicationContext(), R.string.msg_editing_enabled, Toast.LENGTH_LONG).show();
    }

    private void UpdateNote(Note note) {
        if (!mText.getText().toString().equals("")) {
            note.setTitle(mTitle.getText().toString());
            note.setBody(mText.getText().toString());
            note.setDate(DateTimeUtils.currentDateAsString());
            AppCore.getInstance().getDaoSession().getNoteDao().update(note);
            finishUpdate();
            Toast.makeText(getApplicationContext(), "Note Successfully Updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Note Cannot Be Empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void methodShare(String word, String result) {
        final String marketUrl = getResources().getString(R.string.app_url);
        String shareBody = word + "\n" + result + "\n\nUsing app - " + marketUrl;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Result");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void showDeleteConfirmationDialog() {

        new MaterialDialog.Builder(this)
                .title("Delete Confirmation")
                .titleColor(Color.RED)
                .content("Are you sure you want to delete this note?")
                .contentColor(Color.BLACK)
                .positiveText("YES")
                .positiveColor(Color.RED)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        AppCore.getInstance().getDaoSession().getNoteDao().delete(mNote);
                        Toast.makeText(getApplicationContext(), "Note Deleted Successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                    }
                })
                .negativeText("NO")
                .negativeColor(Color.BLACK)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }

    private void finishUpdate() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void loadBannerAd(){
        AdRequest mRequest = new AdRequest.Builder().addTestDevice("Your Device Id Here!").build();
        mAdView.loadAd(mRequest);
    }
}
