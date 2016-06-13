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

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gmail.derry.hussain.data.SecurePreferences;
import com.gmail.derry.hussain.quicknotes.R;
import com.gmail.derry.hussain.quicknotes.core.AppCore;
import com.gmail.derry.hussain.quicknotes.model.PrefKeys;
import com.gmail.derry.hussain.quicknotes.utils.GuiUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.util.Colors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
	
	@BindView(R.id.ad_view) AdView mAdView;
	private SecurePreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPreferences = AppCore.getInstance().getPreferences();
		new GuiUtils().setColorFromPrefs(getSupportActionBar());
        loadAd();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				finish();
				break;
		} return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		finish();
		super.onBackPressed();
	}

    @OnClick(R.id.color_picker)
	public void clickColorPicker() {
		final Dialog dialog = new Dialog(SettingsActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_theme_color_picker);
		
		WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
	    mLayoutParams.copyFrom(dialog.getWindow().getAttributes());
	    mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
	    mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
	    ListView list = (ListView) dialog.findViewById(R.id.colors_list);
	    final String colorNames[] = getResources().getStringArray(R.array.theme_colors_names);
	    final String colorCodes[] = getResources().getStringArray(R.array.theme_colors_hex);
	    list.setAdapter(new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1, colorNames) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView mColorNameText = (TextView) super.getView(position, convertView, parent);
				mColorNameText.setWidth(LayoutParams.MATCH_PARENT);
				mColorNameText.setHeight(LayoutParams.MATCH_PARENT);
				mColorNameText.setBackgroundColor(Color.parseColor(colorCodes[position]));
				mColorNameText.setTextColor(Color.WHITE);
		        return mColorNameText;
			}
		});
	    
	    list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				mPreferences.putInt(PrefKeys.KEY_COLOR, Color.parseColor(colorCodes[arg2]));
				new GuiUtils().setColorFromPrefs(getSupportActionBar());
				dialog.dismiss();
				Intent i = new Intent(getApplicationContext(), SplashActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				finish();
			}
		});
	    
	    dialog.show();
	    dialog.getWindow().setAttributes(mLayoutParams);
	}

    @OnClick(R.id.lyt_rate)
    public void clickRateApp(){
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

	@OnClick(R.id.lyt_libs)
	public void clickLibraries(){
        int iColor = AppCore.getInstance().getPreferences().getInt(PrefKeys.KEY_COLOR, Color.parseColor("#513851"));
        new LibsBuilder().withAutoDetect(false)
                .withLibraries("greendao", "butterknife", "material dialogs")
                .withExcludedLibraries("fastadapter")
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription("Great open source libraries that helped build this app!")
                .withActivityStyle(Libs.ActivityStyle.LIGHT)
                .withActivityColor(new Colors(iColor, iColor))
                .start(this);
	}

    @OnClick(R.id.lyt_about)
    public void clickAbout() {
        String content = getString(R.string.app_description) + "\n\n"
                + getString(R.string.app_copyright);

        new MaterialDialog.Builder(this)
                .iconRes(R.mipmap.ic_launcher)
                .maxIconSize(80)
                .backgroundColorRes(android.R.color.white)
                .title(R.string.app_name)
                .titleColor(mPreferences.getInt(PrefKeys.KEY_COLOR, Color.parseColor("#513851")))
                .content(content)
                .show();
    }

    private void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("Your Device Id Here!").build();
        mAdView.loadAd(adRequest);
    }
}
