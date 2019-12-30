package com.lc.bangumidemo.MyRetrofit.APIinterface

import com.lc.bangumidemo.MyRetrofit.ResClass.BookDetail

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServerdetail {
    @GET("/")
    abstract fun getCall(@Query("xsurl1") bookurl: String?): Call<BookDetail>

}