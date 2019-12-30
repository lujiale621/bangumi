package com.lc.bangumidemo.MyRetrofit.api

import android.util.Log
import android.util.MalformedJsonException
import com.google.gson.JsonParseException
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.ParseException

class ExceptionFunction<T> : Function<Throwable, Observable<T>> {
    override fun apply(@NonNull throwable: Throwable): Observable<T> {
        Log.e("ExceptionFunction", throwable.message)
        return Observable.error(ExceptionEngine().handleException(throwable))
    }
}

/**
 * 错误 异常处理
 */
class ExceptionEngine {

    val UN_KNOWN_ERROR = 1000//未知错误
    val ANALYTIC_SERVER_DATA_ERROR = 1001//解析(服务器)数据错误
    val CONNECT_ERROR = 1002//网络连接错误
    val TIME_OUT_ERROR = 1003//网络连接超时

    fun handleException(e: Throwable): APIException {
        val ex: APIException
        if (e is APIException) {    //服务器返回的错误
            return e
        } else if (e is HttpException) {             //HTTP错误
            ex = APIException(e, e.code())
            ex.msg = "网络错误:" + ex.code
            return ex
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException || e is MalformedJsonException
        ) {  //解析数据错误
            ex = APIException(e, ANALYTIC_SERVER_DATA_ERROR)
            ex.msg = "解析错误"
            return ex
        } else if (e is ConnectException) {//连接网络错误
            ex = APIException(e, CONNECT_ERROR)
            ex.msg = "连接失败"
            return ex
        } else if (e is SocketTimeoutException) {//网络超时
            ex = APIException(e, TIME_OUT_ERROR)
            ex.msg = "网络超时"
            return ex
        } else {  //未知错误
            ex = APIException(e, UN_KNOWN_ERROR)
            ex.msg = e.message
            return ex
        }
    }
}
