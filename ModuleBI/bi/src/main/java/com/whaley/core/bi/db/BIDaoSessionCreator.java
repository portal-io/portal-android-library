package com.whaley.core.bi.db;

import android.database.sqlite.SQLiteDatabase;

import com.whaley.core.debug.logger.Log;
import com.whaleyvr.core.local.db.ClearListener;
import com.whaleyvr.core.local.db.DaoSessionClearListener;
import com.whaleyvr.core.local.db.DaoSessionCreator;

import org.greenrobot.greendao.AbstractDaoSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author: qxw
 * Date: 2017/2/15
 */

public class BIDaoSessionCreator implements DaoSessionCreator {
    private static AbstractDaoSession daoSession;

    @Override
    public AbstractDaoSession onCreateDaoSession(SQLiteDatabase database) {
        if (daoSession == null) {
            daoSession = new DaoMaster(database).newSession();
            DaoSessionClearListener.getInstance().setClearListener(new ClearListener() {
                @Override
                public void onClear() {
                    BIDaoSessionCreator.this.onClear(daoSession);
                }
            });
        }
        return daoSession;
    }


    private void onClear(AbstractDaoSession daoSession) {
        try {
            Class clazz = daoSession.getClass();
            Method method = clazz.getDeclaredMethod("clear");
            method.invoke(daoSession);
        } catch (NoSuchMethodException e) {
            Log.e(e, "BIDaoSessionCreator onClear");
        } catch (InvocationTargetException e) {
            Log.e(e, "BIDaoSessionCreator onClear");
        } catch (IllegalAccessException e) {
            Log.e(e, "BIDaoSessionCreator onClear");
        } catch (Exception e) {
            Log.e(e, "BIDaoSessionCreator onClear");
        }
    }
}
