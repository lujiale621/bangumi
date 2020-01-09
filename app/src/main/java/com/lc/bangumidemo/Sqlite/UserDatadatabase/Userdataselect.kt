package com.lc.bangumidemo.Sqlite.UserDatadatabase

import android.content.Context
import android.util.Log

object Userdataselect {
    fun selectUserdata(
        dbhelper: Userdatahelper
    ): Userdataclass? {
        var i=0
        val db = dbhelper.writableDatabase
        val cursor = db.query("UserData", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val fontsize = cursor.getInt(cursor.getColumnIndex("fontsize"))
                val linesize = cursor.getInt(cursor.getColumnIndex("linesize"))
                val backgroundcolor=cursor.getString(cursor.getColumnIndex("backgroundcolor"))
                val mybackground=cursor.getString(cursor.getColumnIndex("mybackground"))
                i++
                Log.i("Userfontsize",fontsize.toString())
                Log.i("Userlinesize",linesize.toString())
                Log.i("Userbackgroundcolor",backgroundcolor)
                Log.i("Userbackground",mybackground)
                Log.i("Userdatasize",i.toString())
                    return Userdataclass(fontsize, linesize,mybackground,backgroundcolor)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return null
    }
}