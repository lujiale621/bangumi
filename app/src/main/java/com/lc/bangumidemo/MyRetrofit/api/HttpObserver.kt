package com.lc.bangumidemo.MyRetrofit.api

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class HttpObserver<T> : Observer<T> {

    /**
     * 标记是否为特殊情况
     */
    private var resultNull: Boolean = true

    override fun onComplete() {
        // 特殊情况：当请求成功，但T == null时会跳过onNext，仍需当成功处理
        if (resultNull)
            onSuccess(null)
    }

    override fun onSubscribe(d: Disposable) {
        // 可在此处加上dialog, loading...
    }

    override fun onError(e: Throwable) {
        if (e is APIException) {
            onError(e.code, e.msg)
        } else {
            onError(0, e.message)
        }
    }

    override fun onNext(t: T) {
        resultNull = false
        onSuccess(t)

    }

    fun isSuccess(t: T) {
        if (t is SearchResult) {
            if (t.code == 0) {

            }
        }
    }

    abstract fun onSuccess(t: T?)

    /**
     * 统一处理
     *
     * @param code
     * @param msg
     */
    open fun onError(code: Int, msg: String?) {

    }

}
