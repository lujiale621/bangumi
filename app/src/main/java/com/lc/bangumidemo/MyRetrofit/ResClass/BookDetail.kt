package com.lc.bangumidemo.MyRetrofit.ResClass

import java.util.ArrayList

data class BookDetail (
    var data :Bookdata,
    var list :List<Bookdata>,
    var message  :String,
    var code :Int
)
{
    fun getbooknum(): MutableList<String> {
        var  numlist:MutableList<String> = ArrayList<String>()
        for(i in list)
        {
            numlist.add(i.num)
        }
        return numlist
    }
}