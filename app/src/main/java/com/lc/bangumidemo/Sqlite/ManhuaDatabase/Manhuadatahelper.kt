package com.lc.bangumidemo.Sqlite.ManhuaDatabase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class Manhuadatahelper(
                       private val mContext: Context,
                       name: String,
                       factory: SQLiteDatabase.CursorFactory?,
                       version: Int
) : SQLiteOpenHelper(mContext, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        //在数据库创建完成时创建Book表
        db.execSQL(CREATE_MHINDEX)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        //将见表语句定义成字符串常量
        val CREATE_MHINDEX = ("create table MhIndex ("
                + "id integer primary key autoincrement, "
                + "manhuaname text, "
                + "manhuauthor text, "
                + "manhualistsize integer, "
                + "manhuaindex interger, "
                + "manhuacontentindex interger)"
                )

    }

}