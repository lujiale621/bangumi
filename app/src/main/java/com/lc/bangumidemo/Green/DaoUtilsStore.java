package com.lc.bangumidemo.Green;
public class DaoUtilsStore
{
    private volatile static DaoUtilsStore instance = new DaoUtilsStore();
    private CommonDaoUtils<LocalBookData> bookdataDaoUtils;
    private CommonDaoUtils<LocalBookReadClass> bookreaddataDaoUtils;

    public static DaoUtilsStore getInstance()
    {
        return instance;
    }

    private DaoUtilsStore()
    {
        DaoManager mManager = DaoManager.getInstance();
        LocalBookDataDao _BookDataDao = mManager.getDaoSession().getLocalBookDataDao();
        bookdataDaoUtils = new CommonDaoUtils(LocalBookData.class, _BookDataDao);

        LocalBookReadClassDao _BookReadDao = mManager.getDaoSession().getLocalBookReadClassDao();
        bookreaddataDaoUtils = new CommonDaoUtils(LocalBookReadClass.class, _BookReadDao);
    }

    public CommonDaoUtils<LocalBookData> getBookdataDaoUtils()
    {
        return bookdataDaoUtils;
    }

    public CommonDaoUtils<LocalBookReadClass> getBookreaddataDaoUtils()
    {
        return bookreaddataDaoUtils;
    }
}