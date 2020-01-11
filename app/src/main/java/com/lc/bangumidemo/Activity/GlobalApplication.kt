package com.lc.bangumidemo.Activity

import android.app.Application

import com.lc.bangumidemo.Green.DaoManager

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initGreenDao()
    }

    private fun initGreenDao() {
        val mManager = DaoManager.getInstance()
        mManager.init(this)
    }
}
