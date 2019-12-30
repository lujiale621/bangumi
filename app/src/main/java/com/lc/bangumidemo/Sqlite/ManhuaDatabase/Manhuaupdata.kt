package com.lc.bangumidemo.Sqlite.ManhuaDatabase

import android.content.ContentValues


object Manhuaupdata {
    fun updata(dbhelper: Manhuadatahelper, data: ManhuaIndexclass) {
        val db = dbhelper.writableDatabase
        val values = ContentValues()
        //开始组装第一条数据
        values.put("manhuauthor", data.manhuauthor)
        values.put("manhuaname", data.manhuaname)
        values.put("manhuaindex", data.manhuaindex)
        values.put("manhuacontentindex", data.manhuacontentindex)
        values.put("manhualistsize", data.manhualistsize)
        db.update("MhIndex", values, "manhuaname= ? And manhuauthor= ? ", arrayOf(data.manhuaname, data.manhuauthor))//更新数据
        values.clear()//！！！！！

    }
}