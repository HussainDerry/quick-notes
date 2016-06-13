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

package com.gmail.derry.hussain.quicknotes.core;

import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.gmail.derry.hussain.core.ReactorCore;
import com.gmail.derry.hussain.quicknotes.R;
import com.gmail.derry.hussain.quicknotes.database.DaoMaster;
import com.gmail.derry.hussain.quicknotes.database.DaoSession;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

@ReportsCrashes(
        formUri = "Your Cloudant URL",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        formUriBasicAuthLogin = "",
        formUriBasicAuthPassword = "",
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE,
                ReportField.BRAND,
                ReportField.PHONE_MODEL,
                ReportField.DISPLAY,
                ReportField.AVAILABLE_MEM_SIZE,
                ReportField.USER_CRASH_DATE,
                ReportField.THREAD_DETAILS
        },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.acra_crash_msg
)
public class AppCore extends ReactorCore {

    private static AppCore mInstance;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        loadDaoSession();
        MultiDex.install(this);
        mInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mInstance = null;
    }

    public static AppCore getInstance() {
        return mInstance;
    }

    private void loadDaoSession(){
        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession(){
        return mDaoSession;
    }
}
