package com.lc.bangumidemo.MyRetrofit.ResClass

import android.content.Context
import com.lc.bangumidemo.KtUtil.PagesizeUtil.getlinecount
import com.lc.bangumidemo.KtUtil.fontsize
import com.lc.bangumidemo.KtUtil.screenwidth

data class BookContent (
    var content :List<String>,
    var mum :String,
    var message  :String,
    var code :Int
){
     fun getString(context: Context): String {
        var txt:String= String()
        for(i in content) {
            var cutbreak=i.replace(" ", "")
            cutbreak=cutbreak.replace("　", "")
            cutbreak="　　"+cutbreak
            var linecountsize=getlinecount(context, screenwidth, fontsize)
            var resec=(cutbreak.length+2)%linecountsize
            var addsize=linecountsize-resec+2
            if(addsize>=linecountsize){
                addsize= addsize-linecountsize
            }
            while (addsize!=0)
            {
                cutbreak += "　"
                addsize--
            }
            txt += cutbreak
        }
        var newtxt=txt
        return newtxt
    }

}