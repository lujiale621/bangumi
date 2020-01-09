package com.lc.bangumidemo.Sqlite.CollectDatabase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class Collectdbhelper(
    private val mContext: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(mContext, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        //在数据库创建完成时创建Book表
        db.execSQL(CREATE_COLLECT)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        //将见表语句定义成字符串常量
        val CREATE_COLLECT = ("create table CollectData ("
                + "id integer primary key autoincrement, "
                + "name text, "
                + "author text, "
                + "size integer, "
                + "updatatime text, "
                + "tag text, "
                + "cover text, "
                + "url text)"
                )

    }

}