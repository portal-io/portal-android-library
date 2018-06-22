package com.whaley.core.bi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whaleyvr.core.local.db.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Created by dell on 2016/8/23.
 */

public class BIOpenHelper extends DaoMaster.OpenHelper {

    public BIOpenHelper(Context context, String name) {
        super(context, name);
    }

    public BIOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion);
        MigrationHelper.migrate(db, BIBeanDao.class);
    }
}


