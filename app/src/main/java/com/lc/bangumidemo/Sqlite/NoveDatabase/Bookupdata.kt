package com.lc.bangumidemo.Sqlite.NoveDatabase

import android.content.ContentValues

/**
 * val CREATE_BOOKINDEX = ("create table BookDetailActivity ("
 * + "id integer primary key autoincrement, "
 * + "author text, "
 * + "bookname text, "
 * + "hardpageindex interger, "
 * + "hardcontentindex interger, "
 * + "pagecount integer, "
 * + "pageindex interger, "
 * + "contentindex interger)"
 * )
 */
object Bookupdata {
    fun updata(dbhelper: MyDatabaseHelper, data: BookIndexclass) {
        val db = dbhelper.writableDatabase
        val values = ContentValues()
        //开始组装第一条数据
        values.put("author", data.author)
        values.put("bookname", data.bookname)
        values.put("hardpageindex", data.hardpageindex)
        values.put("hardcontentindex", data.hardcontentindex)
        values.put("pagecount", data.pagecount)
        values.put("pageindex", data.pageindex)
        values.put("contentindex", data.contentindex)
        db.update(
            "BookIndex",
            values,
            "bookname= ? And author= ? ",
            arrayOf(data.bookname, data.author)
        )//更新数据
        values.clear()//！！！！！
        Bookselect.selectalldata(dbhelper)
    }
}
