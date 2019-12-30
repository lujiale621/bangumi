package com.lc.bangumidemo.Sqlite.CollectDatabase

import android.content.Context
import android.util.Log

object CollectdataSelect {
    fun selectcollectdata(
        dbhelper: Collectdbhelper,
        data:Collectdataclass
    ): Collectdataclass? {
        var i=0
        val db = dbhelper.writableDatabase
        val cursor = db.query("CollectData", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val size = cursor.getInt(cursor.getColumnIndex("size"))
                val tag = cursor.getString(cursor.getColumnIndex("tag"))
                val url = cursor.getString(cursor.getColumnIndex("url"))
                val updatatime = cursor.getString(cursor.getColumnIndex("updatatime"))
                val cover = cursor.getString(cursor.getColumnIndex("cover"))
                  i++

                Log.i("Collectname",name)
                Log.i("Collectauthor",author)
                Log.i("Collectsize",size.toString())
                Log.i("Collecttag",tag)
                Log.i("Collecturl",url)
                Log.i("Collectupdatatime",updatatime)
                Log.i("Collectcover",cover)
                Log.i("Collectsize",i.toString())
                if (data.name == name && data.author == author) {
                    return Collectdataclass(name, author, size, updatatime, tag, cover, url)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return null
    }
    fun selectcollectalldata(
        context: Context
    ): MutableList<Collectdataclass> {
        var dbhelper = Collectdbhelper(context, "collect.db", null, 1)
        var i=0
        var datalist= mutableListOf<Collectdataclass>()
        val db = dbhelper.writableDatabase
        val cursor = db.query("CollectData", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val size = cursor.getInt(cursor.getColumnIndex("size"))
                val tag = cursor.getString(cursor.getColumnIndex("tag"))
                val url = cursor.getString(cursor.getColumnIndex("url"))
                val updatatime = cursor.getString(cursor.getColumnIndex("updatatime"))
                val cover = cursor.getString(cursor.getColumnIndex("cover"))
                i++

                Log.i("Collectname",name)
                Log.i("Collectauthor",author)
                Log.i("Collectsize",size.toString())
                Log.i("Collecttag",tag)
                Log.i("Collecturl",url)
                Log.i("Collectupdatatime",updatatime)
                Log.i("Collectcover",cover)
                Log.i("Collectsize",i.toString())

                    datalist.add(Collectdataclass(name, author, size, updatatime, tag, cover, url))

            } while (cursor.moveToNext())
        }
        cursor.close()
        dbhelper.close()
        return datalist
    }
}