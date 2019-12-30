package com.lc.bangumidemo.Sqlite.NoveDatabase

import android.content.ContentValues

object Bookinsert {
    fun insertindex(dbhelper: MyDatabaseHelper, data: BookIndexclass) {
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
        db.insert("BookIndex", null, values)//插入第一条数据
        values.clear()//！！！！！
    }

    /**
     * data class BookDataclass (
     * val author:String,
     * val bookname:String,
     * val pagecount:Int,
     * val pageindex:Int,
     * val contentindex:Int
     *
     * )
     * @param dbhelper
     * @param data
     */
    fun insertbookdata(dbhelper: MyDatabaseHelper, data: BookDataclass) {
        val db = dbhelper.writableDatabase
        val values = ContentValues()
        //开始组装第一条数据
        values.put("author", data.author)
        values.put("bookname", data.bookname)
        values.put("pagecount", data.pagecount)
        values.put("pageindex", data.pageindex)
        values.put("content", data.content)
        db.insert("BookData", null, values)//插入第一条数据
        values.clear()//！！！！！
    }

    /**
     * val CREATE_BOOKREAD = ("create table BookRead ("
     * + "id integer primary key autoincrement, "
     * + "author text, "
     * + "bookname text, "
     * + "pagecount integer, "
     * + "data text, "
     * + "pageindex interger, "
     * + "contentindex interger, "
     * + "start interger, "
     * + "end interger, "
     * + "index interger)"
     * )
     * @param dbhelper
     * @param data
     */
    fun insertbookread(dbhelper: MyDatabaseHelper, data: BookReadclass) {
        val db = dbhelper.writableDatabase
        val values = ContentValues()
        //开始组装第一条数据
        values.put("author", data.author)
        values.put("bookname", data.bookname)
        values.put("pagecount", data.pagecount)
        values.put("bookdata", data.bookdata)
        values.put("pageindex", data.pageindex)
        values.put("contentindex", data.contentindex)
        values.put("start", data.start)
        values.put("end", data.end)
        values.put("indexx", data.indexx)
        db.insert("BookRead", null, values)//插入第一条数据
        values.clear()//！！！！！

    }
}
