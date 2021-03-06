package com.lc.bangumidemo.Sqlite.UserDatadatabase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.lc.bangumidemo.KtUtil.isfirst

class Userdatahelper(
    private val mContext: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(mContext, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        //在数据库创建完成时创建Book表
        db.execSQL(CREATE_UserData)
        isfirst=true
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        //将见表语句定义成字符串常量
        val CREATE_UserData = ("create table UserData ("
                + "id integer primary key autoincrement, "
                + "fontsize integer, "
                + "linesize integer, "
                + "mybackground text, "
                + "backgroundcolor text)"
                )

    }

}