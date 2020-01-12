package com.lc.bangumidemo.Green;
public class DaoUtilsStore
{
    private volatile static DaoUtilsStore instance = new DaoUtilsStore();
    private CommonDaoUtils<LocalBookData> bookdataDaoUtils;
    private CommonDaoUtils<LocalBookReadClass> bookreaddataDaoUtils;
    private CommonDaoUtils<LocalBookIndex> bookindexDaoUtils;
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


        LocalBookIndexDao _BookIndexDao = mManager.getDaoSession().getLocalBookIndexDao();
        bookindexDaoUtils = new CommonDaoUtils(LocalBookIndex.class, _BookIndexDao);
    }

    public CommonDaoUtils<LocalBookData> getBookdataDaoUtils()
    {
        return bookdataDaoUtils;
    }

    public CommonDaoUtils<LocalBookReadClass> getBookreaddataDaoUtils() { return bookreaddataDaoUtils; }

    public CommonDaoUtils<LocalBookIndex> getBookindexDaoUtils() { return bookindexDaoUtils; }
}