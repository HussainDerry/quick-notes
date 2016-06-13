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

package com.gmail.derry.hussain.quicknotes.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.gmail.derry.hussain.quicknotes.model.Note;


/**
 * {@inheritDoc}
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteDaoConfig;

    private final NoteDao noteDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        noteDao = new NoteDao(noteDaoConfig, this);

        registerDao(Note.class, noteDao);
    }
    
    public void clear() {
        noteDaoConfig.getIdentityScope().clear();
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

}
