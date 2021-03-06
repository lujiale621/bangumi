package com.lc.bangumidemo.Green;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.lc.bangumidemo.Green.LocalBookData;
import com.lc.bangumidemo.Green.LocalBookReadClass;
import com.lc.bangumidemo.Green.LocalBookIndex;

import com.lc.bangumidemo.Green.LocalBookDataDao;
import com.lc.bangumidemo.Green.LocalBookReadClassDao;
import com.lc.bangumidemo.Green.LocalBookIndexDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig localBookDataDaoConfig;
    private final DaoConfig localBookReadClassDaoConfig;
    private final DaoConfig localBookIndexDaoConfig;

    private final LocalBookDataDao localBookDataDao;
    private final LocalBookReadClassDao localBookReadClassDao;
    private final LocalBookIndexDao localBookIndexDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        localBookDataDaoConfig = daoConfigMap.get(LocalBookDataDao.class).clone();
        localBookDataDaoConfig.initIdentityScope(type);

        localBookReadClassDaoConfig = daoConfigMap.get(LocalBookReadClassDao.class).clone();
        localBookReadClassDaoConfig.initIdentityScope(type);

        localBookIndexDaoConfig = daoConfigMap.get(LocalBookIndexDao.class).clone();
        localBookIndexDaoConfig.initIdentityScope(type);

        localBookDataDao = new LocalBookDataDao(localBookDataDaoConfig, this);
        localBookReadClassDao = new LocalBookReadClassDao(localBookReadClassDaoConfig, this);
        localBookIndexDao = new LocalBookIndexDao(localBookIndexDaoConfig, this);

        registerDao(LocalBookData.class, localBookDataDao);
        registerDao(LocalBookReadClass.class, localBookReadClassDao);
        registerDao(LocalBookIndex.class, localBookIndexDao);
    }
    
    public void clear() {
        localBookDataDaoConfig.clearIdentityScope();
        localBookReadClassDaoConfig.clearIdentityScope();
        localBookIndexDaoConfig.clearIdentityScope();
    }

    public LocalBookDataDao getLocalBookDataDao() {
        return localBookDataDao;
    }

    public LocalBookReadClassDao getLocalBookReadClassDao() {
        return localBookReadClassDao;
    }

    public LocalBookIndexDao getLocalBookIndexDao() {
        return localBookIndexDao;
    }

}
