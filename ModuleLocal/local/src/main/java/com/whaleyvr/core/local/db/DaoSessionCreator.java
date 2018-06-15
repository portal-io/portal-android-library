package com.whaleyvr.core.local.db;

import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.AbstractDaoSession;

public interface DaoSessionCreator {
    AbstractDaoSession onCreateDaoSession(SQLiteDatabase database);
}
