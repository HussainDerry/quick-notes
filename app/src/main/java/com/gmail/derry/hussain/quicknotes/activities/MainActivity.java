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

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.derry.hussain.quicknotes.R;
import com.gmail.derry.hussain.quicknotes.adapter.DrawerListAdapter;
import com.gmail.derry.hussain.quicknotes.core.AppCore;
import com.gmail.derry.hussain.quicknotes.fragments.NewNoteFragment;
import com.gmail.derry.hussain.quicknotes.fragments.ShowAllNotesFragment;
import com.gmail.derry.hussain.quicknotes.model.DrawerItem;
import com.gmail.derry.hussain.quicknotes.utils.GuiUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    /* View Elements */
	@BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
	@BindView(R.id.drawer_menu) ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
    private InterstitialAd mInterstitialAd;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

    /* Drawer Elements */
	private String[] drawerMenuTitlesArray;
	private TypedArray drawerMenuIconsArray;
	private ArrayList<DrawerItem> drawerItemsArrayList;
	private DrawerListAdapter mDrawerMenuListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        makeActionOverflowMenuShown();
        setupDrawerTitle();
        setupInterstitialAd();
        loadMenuData();
        loadDrawerItemsArray();
        setupDrawer();
		setuptActionBar();

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null,
                R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				setActionBarTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				setActionBarTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.addDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			displayView(0);
		}
	}

	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		setActionBarTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

    public void openDrawer(){
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private void setActionBarTitle(CharSequence title){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(title);
        }
    }

	private void makeActionOverflowMenuShown() {
	    try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if (menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        Log.d("OK", e.getLocalizedMessage());
	    }
	}

	private void showInterstitial() {
		if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		}
	}

    private void setupInterstitialAd(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("Your Device Id Here!").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void setuptActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
            new GuiUtils().setColorFromPrefs(actionBar);
        }
    }

    private void loadMenuData(){
        drawerMenuTitlesArray = getResources().getStringArray(R.array.nav_drawer_items);
        drawerMenuIconsArray = getResources().obtainTypedArray(R.array.nav_drawer_icons);
    }

    private void loadDrawerItemsArray(){
        drawerItemsArrayList = new ArrayList<>();
        drawerItemsArrayList.add(new DrawerItem(drawerMenuTitlesArray[0], drawerMenuIconsArray.getResourceId(0, -1), true, getNotesCount()));
        drawerItemsArrayList.add(new DrawerItem(drawerMenuTitlesArray[1], drawerMenuIconsArray.getResourceId(1, -1)));
        drawerItemsArrayList.add(new DrawerItem(drawerMenuTitlesArray[2], drawerMenuIconsArray.getResourceId(2, -1)));
        drawerMenuIconsArray.recycle();
    }

    private void setupDrawer(){
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        mDrawerMenuListAdapter = new DrawerListAdapter(getApplicationContext(), drawerItemsArrayList);
        mDrawerList.setAdapter(mDrawerMenuListAdapter);
    }

    private void setupDrawerTitle(){
        mTitle = mDrawerTitle = getTitle();
    }

    private String getNotesCount(){
        int iNotesCount = AppCore.getInstance().getDaoSession().getNoteDao().loadAll().size();
        if(iNotesCount > 500){
            return "100+";
        }else{
            return iNotesCount + "";
        }
    }

	private void displayView(int position) {
		// update the main content by replacing fragments
		showInterstitial();

		drawerItemsArrayList.get(1).setCount(getNotesCount());
		mDrawerMenuListAdapter.notifyDataSetChanged();

		Fragment fragment = null;
		switch (position) {
			case 0:
				fragment = new ShowAllNotesFragment();
				break;
			case 1:
				fragment = new NewNoteFragment();
				break;
			case 2:
				Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				finish();
				break;
			default:
				break;
		}

		if (fragment != null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();

			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(drawerMenuTitlesArray[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}

		mDrawerLayout.closeDrawers();
	}
}
