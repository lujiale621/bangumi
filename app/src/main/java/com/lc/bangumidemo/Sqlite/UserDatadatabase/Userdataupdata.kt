package com.lc.bangumidemo.Sqlite.UserDatadatabase

import android.content.ContentValues
import android.content.Context
import com.lc.bangumidemo.KtUtil.backgroundcolor
import com.lc.bangumidemo.KtUtil.fontsize
import com.lc.bangumidemo.KtUtil.linesize

object Userdataupdata {
    fun updata(dbhelper: Userdatahelper, data: Userdataclass) {
        val db = dbhelper.writableDatabase
        val values = ContentValues()
        //开始组装第一条数据
        values.put("fontsize", data.fontsize)
        values.put("linesize", data.linesize)
        values.put("backgroundcolor", data.backgroundcolor)
        db.update("UserData", values, null,null)//更新数据
        values.clear()//！！！！！
    }
     fun updatauserdata(context: Context) {
        var userdbhelper= Userdatahelper(context,"user.db",null,1)
         updata(userdbhelper, Userdataclass(fontsize, linesize, backgroundcolor))
         Userdataselect.selectUserdata(userdbhelper)

         userdbhelper.close()
    }
}