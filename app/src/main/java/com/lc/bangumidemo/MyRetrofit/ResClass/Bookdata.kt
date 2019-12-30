package com.lc.bangumidemo.MyRetrofit.ResClass

import android.graphics.Bitmap
import com.lc.bangumidemo.KtUtil.imglist

data class Bookdata (
    var name: String,
    var url:String,
    var cover:String,
    var introduce:String,
    var time :String,
    var num:String,
    var tag:String,
    var author :String,
    var status :String
)
{
    fun getmap(i:Int): Bitmap
    {
        return imglist[i]
    }

}