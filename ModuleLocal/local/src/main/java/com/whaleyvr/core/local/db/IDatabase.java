package com.whaleyvr.core.local.db;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collection;
import java.util.List;

public interface IDatabase<M, K> {


    boolean insert(M m);

    boolean delete(M m);

    boolean deleteByKey(K key);

    boolean deleteList(List<M> mList);

    boolean deleteByKeyInTx(K... key);

    boolean deleteAll();

    boolean insertOrReplace(M m);

    boolean update(M m);

    boolean updateInTx(M... m);

    boolean updateList(List<M> mList);

    M load(K key);

    List<M> loadAll();

    boolean refresh(M m);


    /**
     * 清理缓存
     */
    void clearDaoSession();

    /**
     * Delete all tables and content from our database
     */
    boolean dropDatabase();

    /**
     * 事务
     */
    void runInTx(Runnable runnable);

    /**
     * 获取Dao
     *
     * @return
     */
    AbstractDao<M, K> getAbstractDao();

    /**
     * 添加集合
     *
     * @param mList
     */
    boolean insertList(List<M> mList);

    /**
     * 添加集合
     *
     * @param mList
     */
    boolean insertOrReplaceList(List<M> mList);

    /**
     * 自定义查询
     *
     * @return
     */
    QueryBuilder<M> getQueryBuilder();

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    List<M> queryRaw(String where, String... selectionArg);

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    Query<M> queryRawCreate(String where, Object... selectionArg);

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    Query<M> queryRawCreateListArgs(String where, Collection<Object> selectionArg);
}