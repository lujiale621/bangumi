package com.lc.bangumidemo.MyRetrofit.api

import io.reactivex.Observable

class ManhuaLoader: ObjectLoader() {
    private val mManhuaService =
        RetrofitManager.create(APIService::class.java)

    fun getManhuaDetail(mhua1: String): Observable<ManhuaDetailResult> {
        return observe(mManhuaService.getManhuaDetail(mhua1))
    }

    fun getManhua(mhuaname: String): Observable<ManhuaSearchResult> {
        return observe(mManhuaService.getManhua(mhuaname))
    }
    fun getManhuaContent(mhua2: String): Observable<ManhuaContentResult> {
        return observe(mManhuaService.getManhuaContent(mhua2))
    }
}