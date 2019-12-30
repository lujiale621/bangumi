package com.lc.bangumidemo.Sqlite.ManhuaDatabase

import android.content.Context
import android.util.Log
import com.lc.bangumidemo.KtUtil.mhuaDetail

object ManhuaSelect {

    fun selectManhuaIndex(
        dbhelper: Manhuadatahelper,
        data:ManhuaIndexclass
    ): ManhuaIndexclass? {
        var i=0
        val db = dbhelper.writableDatabase
        val cursor = db.query("MhIndex", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                val manhuauthor = cursor.getString(cursor.getColumnIndex("manhuauthor"))
                val manhuaname = cursor.getString(cursor.getColumnIndex("manhuaname"))
                val manhualistsize = cursor.getInt(cursor.getColumnIndex("manhualistsize"))
                val manhuaindex = cursor.getInt(cursor.getColumnIndex("manhuaindex"))
                val manhuacontentindex = cursor.getInt(cursor.getColumnIndex("manhuacontentindex"))
                i++
                Log.i("Manhuamanhuaname",manhuaname)
                Log.i("manhuauthor",manhuauthor)
                Log.i("manhualistsize",manhualistsize.toString())
                Log.i("manhuaindex",manhuaindex.toString())
                Log.i("manhuacontentindex",manhuacontentindex.toString())
                Log.i("manhuaindexsize",i.toString())
                if (data.manhuauthor == manhuauthor && data.manhuaname == manhuaname) {
                    return ManhuaIndexclass(
                        manhuaname,
                        manhuauthor,
                        manhualistsize,
                        manhuaindex,
                        manhuacontentindex)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return null
    }
    fun selectmanhuaindex(context: Context):ManhuaIndexclass?{
        //查询索引
        var db= Manhuadatahelper(context,"manhua.db",null,1)
        var selectindex= ManhuaIndexclass(
            mhuaDetail!!.data.name, mhuaDetail!!.data.author,
            mhuaDetail!!.list.size,0,0)
        var result=ManhuaSelect.selectManhuaIndex(db,selectindex)
        return if(result!=null){
            result
        } else{
            null
        }
        db.close()
    }
}