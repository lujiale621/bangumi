package com.lc.bangumidemo.MyRetrofit.api

import io.reactivex.Observable

class NovelLoader : ObjectLoader() {
    private val mMovieService =
        RetrofitManager.create(APIService::class.java)

    fun getNovels(xsname: String): Observable<SearchResult> {
        return observe(mMovieService.getNovels(xsname))
    }

    fun getNovelDetail(xsurl1: String): Observable<DetailResult> {
        return observe(mMovieService.getNovelDetail(xsurl1))
    }

    fun getNovelContent(xsurl2: String): Observable<ContentResult> {
        return observe(mMovieService.getNovelContent(xsurl2))
    }
}
