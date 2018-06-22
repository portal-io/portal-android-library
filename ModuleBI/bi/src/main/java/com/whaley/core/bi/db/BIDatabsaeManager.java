package com.whaley.core.bi.db;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.bi.model.LogInfoParam;
import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.core.local.db.AbstractDatabaseManager;
import com.whaleyvr.core.local.db.DaoSessionClearListener;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * Author: qxw
 * Date: 2016/11/7
 */

public class BIDatabsaeManager extends AbstractDatabaseManager<BIBean, String> {

    public static BIDatabsaeManager sInstance;

    public BIDatabsaeManager() {
        initOpenHelper(AppContextProvider.getInstance().getContext(), new BIDBOpenHelperCreator(), new BIDaoSessionCreator(), DaoSessionClearListener.getInstance());
    }


    public DaoSession getSession() {
        return (DaoSession) getDaoSession();
    }


    public BIOpenHelper getOpenHelper() {
        return (BIOpenHelper) getHelper();
    }

    public synchronized static BIDatabsaeManager getInstance() {
        if (sInstance == null) {
            sInstance = new BIDatabsaeManager();
        }
        return sInstance;
    }

    @Override
    public AbstractDao<BIBean, String> getAbstractDao() {
        return getSession().getBIBeanDao();
    }


    public boolean insert(String metadata, LogInfoParam logInfoParam) {
        return insert(new BIBean(Long.toString(System.currentTimeMillis()), metadata, GsonUtil.getGson().toJson(logInfoParam)));
    }

    public List<BIBean> queryAllList() {
        return getQueryBuilder().list();
    }

//    public BIBean queryLast() {
//        return getQueryBuilder().orderDesc();
//    }

    public long queryCount() {
        try {
            return getQueryBuilder().count();
        } catch (Exception e) {

        }
        return 0;
    }

    public boolean deleteListByIds(List<BIBean> ids) {
        return deleteList(ids);
    }

}
