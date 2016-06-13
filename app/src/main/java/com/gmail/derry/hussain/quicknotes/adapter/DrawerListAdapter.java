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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.derry.hussain.quicknotes.R;
import com.gmail.derry.hussain.quicknotes.model.DrawerItem;

public class DrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<DrawerItem> drawerItems;
	
	public DrawerListAdapter(Context context, ArrayList<DrawerItem> drawerItems){
		this.context = context;
		this.drawerItems = drawerItems;
	}

	@Override
	public int getCount() {
		return drawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return drawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
         
        ImageView mIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView mTitle = (TextView) convertView.findViewById(R.id.title);
        TextView mCount = (TextView) convertView.findViewById(R.id.counter);
         
        mIcon.setImageResource(drawerItems.get(position).getIcon());
        mTitle.setText(drawerItems.get(position).getTitle());
        
        if(drawerItems.get(position).getCounterVisibility()){
        	mCount.setText(drawerItems.get(position).getCount());
        }else{
        	mCount.setVisibility(View.GONE);
        }
        
        return convertView;
	}

}
