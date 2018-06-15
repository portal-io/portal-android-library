package com.whaleyvr.core.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collection;
import java.util.List;

public abstract class AbstractDatabaseManager<M, K> implements IDatabase<M, K> {

    private static final String DEFAULT_DATABASE_NAME = "cfpamf.db";

    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private  DatabaseOpenHelper mHelper;
    private   AbstractDaoSession daoSession;
    private  DaoSessionCreator daoSessionCreator;
    private  DBOpenHelperCreator dbOpenHelperCreator;
    private  ClearListener clearListener;

    public  void setClearListener(ClearListener clearListener) {
        this.clearListener = clearListener;
    }

    public void setDaoSessionCreator(DaoSessionCreator daoSessionCreator) {
        this.daoSessionCreator = daoSessionCreator;
    }

    public void setDbOpenHelperCreator(DBOpenHelperCreator dbOpenHelperCreator) {
        this.dbOpenHelperCreator = dbOpenHelperCreator;
    }

    public AbstractDaoSession getDaoSession() {
        return daoSession;
    }

    public DatabaseOpenHelper getHelper() {
        return mHelper;
    }

    /**
     * 初始化OpenHelper
     *
     * @param context
     */
    public  void initOpenHelper(@NonNull Context context,DBOpenHelperCreator dbOpenHelperCreator,DaoSessionCreator daoSessionCreator,ClearListener clearListener) {
        setDbOpenHelperCreator(dbOpenHelperCreator);
        setDaoSessionCreator(daoSessionCreator);
        setClearListener(clearListener);
        mHelper = getOpenHelper(context);
        openWritableDb();
    }

    /**
     * Query for readable DB
     */
    protected  void openReadableDb() throws SQLiteException {
        daoSession = daoSessionCreator.onCreateDaoSession(getReadableDatabase());
//                new DaoMaster(getReadableDatabase()).newSession();
    }

    /**
     * Query for writable DB
     */
    protected  void openWritableDb() throws SQLiteException {
        daoSession = daoSessionCreator.onCreateDaoSession(getWritableDatabase());
//                new DaoMaster(getWritableDatabase()).newSession();
    }

    /**
     * @return
     */
    private  SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase();
    }

    /**
     * @return
     */
    private  SQLiteDatabase getReadableDatabase() {
        return mHelper.getReadableDatabase();
    }

    /**
     * 在applicaiton中初始化DatabaseHelper
     */
    private  DatabaseOpenHelper getOpenHelper(@NonNull Context context) {
        closeDbConnections();
        return dbOpenHelperCreator.onCreateDBOpenHelper(context);
//                new MyOpenHelper(context, dataBaseName, null);
    }

    /**
     * 只关闭helper就好,看源码就知道helper关闭的时候会关闭数据库
     */
    public  void closeDbConnections() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (clearListener != null) {
            clearListener.onClear();
            clearListener = null;
        }
        daoSession=null;
    }

    @Override
    public void clearDaoSession() {
        if (clearListener != null) {
            clearListener.onClear();
            clearListener = null;
        }
        daoSession=null;
    }

    @Override
    public boolean dropDatabase() {
        try {
            openWritableDb();
            // DaoMaster.dropAllTables(database, true); // drops all tables
            // mHelper.onCreate(database); // creates the tables
//			daoSession.deleteAll(BankCardBean.class); // clear all elements
            // from
            // a table
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean insert(@NonNull M m) {
        try {
            if (m == null)
                return false;
            openWritableDb();
            getAbstractDao().insert(m);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean insertOrReplace(@NonNull M m) {
        try {
            if (m == null)
                return false;
            openWritableDb();
            getAbstractDao().insertOrReplace(m);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(@NonNull M m) {
        try {
            if (m == null)
                return false;
            openWritableDb();
            getAbstractDao().delete(m);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteByKey(K key) {
        try {
            if (TextUtils.isEmpty(key.toString()))
                return false;
            openWritableDb();
            getAbstractDao().deleteByKey(key);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteByKeyInTx(K... key) {
        try {
            openWritableDb();
            getAbstractDao().deleteByKeyInTx(key);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteList(List<M> mList) {
        try {
            if (ListUtils.isEmpty(mList))
                return false;
            openWritableDb();
            getAbstractDao().deleteInTx(mList);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteAll() {
        try {
            openWritableDb();
            getAbstractDao().deleteAll();
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update(@NonNull M m) {
        try {
            if (m == null)
                return false;
            openWritableDb();
            getAbstractDao().update(m);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateInTx(M... m) {
        try {
            if (m == null)
                return false;
            openWritableDb();
            getAbstractDao().updateInTx(m);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateList(List<M> mList) {
        try {
            if (ListUtils.isEmpty(mList))
                return false;
            openWritableDb();
            getAbstractDao().updateInTx(mList);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public M load(@NonNull K key) {
        try {
            openReadableDb();
            return getAbstractDao().load(key);
        } catch (SQLiteException e) {
            return null;
        }
    }

    @Override
    public List<M> loadAll() {
        openReadableDb();
        return getAbstractDao().loadAll();
    }

    @Override
    public boolean refresh(@NonNull M m) {
        try {
            if (m == null)
                return false;
            openWritableDb();
            getAbstractDao().refresh(m);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public void runInTx(Runnable runnable) {
        try {
            openWritableDb();
            daoSession.runInTx(runnable);
        } catch (SQLiteException e) {
        }
    }

    @Override
    public boolean insertList(@NonNull List<M> list) {
        try {
            if (ListUtils.isEmpty(list))
                return false;
            openWritableDb();
            getAbstractDao().insertInTx(list);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    /**
     * @param list
     * @return
     */
    @Override
    public boolean insertOrReplaceList(@NonNull List<M> list) {
        try {
            if (ListUtils.isEmpty(list))
                return false;
            openWritableDb();
            getAbstractDao().insertOrReplaceInTx(list);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    /**
     * @return
     */
    @Override
    public QueryBuilder<M> getQueryBuilder() {
        openReadableDb();
        return getAbstractDao().queryBuilder();
    }

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    @Override
    public List<M> queryRaw(String where, String... selectionArg) {
        openReadableDb();
        return getAbstractDao().queryRaw(where, selectionArg);
    }

    @Override
    public Query<M> queryRawCreate(String where, Object... selectionArg) {
        openReadableDb();
        return getAbstractDao().queryRawCreate(where, selectionArg);
    }

    @Override
    public Query<M> queryRawCreateListArgs(String where, Collection<Object> selectionArg) {
        openReadableDb();
        return getAbstractDao().queryRawCreateListArgs(where, selectionArg);
    }

}
