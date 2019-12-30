package com.lc.bangumidemo.MyRetrofit.ResClass
data class BookContent (
    var content :List<String>,
    var mum :String,
    var message  :String,
    var code :Int
){
     fun getString(): String {
        var txt:String= String()
        for(i in content) {
            var cutbreak=i.replace(" ", "")
            txt += cutbreak
        }
        var newtxt=txt
        return newtxt
    }

}