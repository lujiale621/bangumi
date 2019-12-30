package com.lc.bangumidemo.MyRetrofit.APIinterface

import com.lc.bangumidemo.MyRetrofit.ResClass.BookResult

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("/")
    fun getCall(@Query("xsname") bookname: String): Call<BookResult>

}

