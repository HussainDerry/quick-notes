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

package com.gmail.derry.hussain.quicknotes.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;

import com.gmail.derry.hussain.quicknotes.core.AppCore;
import com.gmail.derry.hussain.quicknotes.model.PrefKeys;

public class GuiUtils {

    public GuiUtils(){
        super();
    }

	public ActionBar setColorFromPrefs(ActionBar actionbar){
		ActionBar action_new = actionbar;
		int color = AppCore.getInstance().getPreferences().getInt(PrefKeys.KEY_COLOR, Color.parseColor("#513851"));
		ColorDrawable colordrw = new ColorDrawable(color);
		action_new.setBackgroundDrawable(colordrw);

		return action_new;
	}
	
	
}
