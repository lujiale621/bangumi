package com.lc.bangumidemo.MyRetrofit.api

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


class MovieLoader : ObjectLoader() {
    private val mMovieService =
        RetrofitManager.create(APIService::class.java)


    fun getMovieDetail(ysurl: String): Observable<DetailResult> {
        return observe(mMovieService.getBangumiDetail(ysurl))
    }

    fun getMovies(ysname: String): Observable<SearchResult> {
        return observe(mMovieService.getBangumis(ysname))
    }
}

fun Disposable.addTo(c: CompositeDisposable) {
    c.add(this)
}
