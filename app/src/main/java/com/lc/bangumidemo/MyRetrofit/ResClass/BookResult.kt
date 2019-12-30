package com.lc.bangumidemo.MyRetrofit.ResClass

data class BookResult (
    var list :List<Bookdata>,
    var message:String,
    var code: Int
)