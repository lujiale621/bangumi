package com.lc.bangumidemo.Sqlite.ManhuaDatabase

import android.content.ContentValues


object ManhuaInsert {
    fun insertindex(dbhelper: Manhuadatahelper, data:ManhuaIndexclass ) {
        val db = dbhelper.writableDatabase
        val values = ContentValues()
        //开始组装第一条数据
        values.put("manhuauthor", data.manhuauthor)
        values.put("manhuaname", data.manhuaname)
        values.put("manhuaindex", data.manhuaindex)
        values.put("manhuacontentindex", data.manhuacontentindex)
        values.put("manhualistsize", data.manhualistsize)
        db.insert("MhIndex", null, values)//插入第一条数据
        values.clear()//！！！！！
    }
}