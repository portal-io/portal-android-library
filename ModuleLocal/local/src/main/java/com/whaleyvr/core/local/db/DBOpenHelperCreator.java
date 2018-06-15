package com.whaleyvr.core.local.db;

import android.content.Context;

import org.greenrobot.greendao.database.DatabaseOpenHelper;

public interface DBOpenHelperCreator {
    DatabaseOpenHelper onCreateDBOpenHelper(Context context);
}
