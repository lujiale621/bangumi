package com.lc.bangumidemo.Sqlite.CollectDatabase

import android.content.ContentValues

object CollectdataInsert{
    fun insertcollectdata(dbhelper: Collectdbhelper, data:Collectdataclass ) {
        val db = dbhelper.writableDatabase
        val values = ContentValues()
        //开始组装第一条数据
        values.put("author", data.author)
        values.put("name", data.name)
        values.put("tag", data.tag)
        values.put("updatatime", data.updatatime)
        values.put("size", data.size)
        values.put("cover", data.cover)
        values.put("url", data.url)
        db.insert("CollectData", null, values)//插入第一条数据
        values.clear()//！！！！！
    }
}