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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import com.gmail.derry.hussain.data.SecurePreferences;
import com.gmail.derry.hussain.quicknotes.R;
import com.gmail.derry.hussain.quicknotes.core.AppCore;
import com.gmail.derry.hussain.quicknotes.database.NoteDao;
import com.gmail.derry.hussain.quicknotes.model.Note;
import com.gmail.derry.hussain.quicknotes.model.PrefKeys;
import com.gmail.derry.hussain.utils.DateTimeUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

	@BindView(R.id.color_overlay) LinearLayout mLinearLayout;

	private SecurePreferences mPreferences;
	private NoteDao mDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		ButterKnife.bind(this);

		loadDataPersistenceObjects();
		colorBackground();
		fillExampleNotes();
		runDelayTask();
	}
	
	private void fillExampleNotes(){
		if(mPreferences.getBoolean(PrefKeys.KEY_FIRST_START, true)){
			for (int i = 0; i < 1; i++) {
				Note n = new Note();
				n.setTitle(getResources().getStringArray(R.array.notes_titles)[i]);
				n.setBody(getResources().getStringArray(R.array.notes_content)[i]);
				n.setDate(DateTimeUtils.currentDateAsString());
				mDao.insert(n);
			}
			mPreferences.putBoolean(PrefKeys.KEY_FIRST_START, false);
		}
	}
	
	private void colorBackground(){
		int iCurrentColor = mPreferences.getInt(PrefKeys.KEY_COLOR, Color.parseColor("#513851"));
		String hexColor = String.format("#%06X", (0xFFFFFF & iCurrentColor));
		String newColor = "#BF" + hexColor.substring(1, 7);
		mLinearLayout.setBackgroundColor(Color.parseColor(newColor));
	}

	private void loadDataPersistenceObjects(){
		mPreferences = AppCore.getInstance().getPreferences();
		mDao = AppCore.getInstance().getDaoSession().getNoteDao();
	}

	private void runDelayTask(){
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				finish();
			}

		};

		new Timer().schedule(task, 1500);
	}
}
