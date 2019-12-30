package com.lc.bangumidemo.MyRetrofit.APIinterface

import com.lc.bangumidemo.MyRetrofit.ResClass.BookContent
import com.lc.bangumidemo.MyRetrofit.ResClass.BookDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServerread {
    @GET("/")
    abstract fun getCall(@Query("xsurl2") hrefurl: String?): Call<BookContent>
}