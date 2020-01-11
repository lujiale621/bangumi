package com.lc.bangumidemo.Activity;

import android.app.Application;

import com.lc.bangumidemo.Green.DaoManager;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
    }

    private void initGreenDao() {
        DaoManager mManager = DaoManager.getInstance();
        mManager.init(this);
    }
}
