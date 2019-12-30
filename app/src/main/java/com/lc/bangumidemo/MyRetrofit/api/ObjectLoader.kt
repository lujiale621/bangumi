package com.lc.bangumidemo.MyRetrofit.api

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class ObjectLoader {
    fun <T> observe(observable: Observable<T>): Observable<T> {
        return observable
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
