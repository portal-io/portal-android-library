package com.whaley.core.bi.db;

import android.content.Context;


import com.whaley.core.bi.model.BIConstants;
import com.whaleyvr.core.local.db.DBOpenHelperCreator;

import org.greenrobot.greendao.database.DatabaseOpenHelper;

/**
 * Author: qxw
 * Date: 2017/2/15
 */

public class BIDBOpenHelperCreator implements DBOpenHelperCreator {

    public static BIOpenHelper myOpenHelper;

    @Override
    public DatabaseOpenHelper onCreateDBOpenHelper(Context context) {
        if(myOpenHelper==null){
            myOpenHelper=new BIOpenHelper(context, BIConstants.DB_NAME, null);
        }
        return myOpenHelper;
    }
}
