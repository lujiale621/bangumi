package com.lc.bangumidemo.Sqlite.UserDatadatabase

import android.content.ContentValues

object Userdatainsert {
    fun insertuserdata(dbhelper: Userdatahelper, data:Userdataclass ) {
        val db = dbhelper.writableDatabase
        val values = ContentValues()
        //开始组装第一条数据
        values.put("fontsize", data.fontsize)
        values.put("linesize", data.linesize)
        db.insert("UserData", null, values)//插入第一条数据
        values.clear()//！！！！！
    }
}