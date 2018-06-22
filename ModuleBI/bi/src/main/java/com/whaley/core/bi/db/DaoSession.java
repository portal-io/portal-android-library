package com.whaley.core.bi.db;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bIBeanDaoConfig;

    private final BIBeanDao bIBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bIBeanDaoConfig = daoConfigMap.get(BIBeanDao.class).clone();
        bIBeanDaoConfig.initIdentityScope(type);

        bIBeanDao = new BIBeanDao(bIBeanDaoConfig, this);

        registerDao(BIBean.class, bIBeanDao);
    }
    
    public void clear() {
        bIBeanDaoConfig.clearIdentityScope();
    }

    public BIBeanDao getBIBeanDao() {
        return bIBeanDao;
    }

}
