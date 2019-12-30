package com.lc.bangumidemo.Sqlite.NoveDatabase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(
    private val mContext: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(mContext, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        //在数据库创建完成时创建Book表
        db.execSQL(CREATE_BOOKINDEX)
        db.execSQL(CREATE_BOOKDATA)
        db.execSQL(CREATE_BOOKREAD)
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show()

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        //将见表语句定义成字符串常量
        val CREATE_BOOKINDEX = ("create table BookIndex ("
                + "id integer primary key autoincrement, "
                + "author text, "
                + "bookname text, "
                + "hardpageindex interger, "
                + "hardcontentindex interger, "
                + "pagecount integer, "
                + "pageindex interger, "
                + "contentindex interger)"
                )
        val CREATE_BOOKDATA = ("create table BookData ("
                + "id integer primary key autoincrement, "
                + "author text, "
                + "bookname text, "
                + "content text, "
                + "contentsize integer, "
                + "pagecount integer, "
                + "pageindex interger)"
                )
        val CREATE_BOOKREAD = ("create table BookRead ("
                + "id integer primary key autoincrement, "
                + "author text, "
                + "bookname text, "
                + "pagecount integer, "
                + "bookdata text, "
                + "pageindex interger, "
                + "contentindex interger, "
                + "start interger, "
                + "end interger, "
                + "indexx interger)"
                )
    }

}