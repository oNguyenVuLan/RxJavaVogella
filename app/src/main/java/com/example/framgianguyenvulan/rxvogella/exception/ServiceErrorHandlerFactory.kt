package com.example.framgianguyenvulan.rxvogella.exception

import android.util.Log
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/30/17.
 */
class ServiceErrorHandlerFactory : CallAdapter.Factory() {

    var original: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit): CallAdapter<*, Any> {
        return RxCallAdapterWrapper(retrofit, original.get(returnType,annotations, retrofit)!!
        )
    }

    class RxCallAdapterWrapper<R>( retrofit: Retrofit,wrapped: CallAdapter<R, Any>) : CallAdapter<R, Any> {
        var retrofit: Retrofit? = retrofit
        var wrapped: CallAdapter<R, Any>? =wrapped
        override fun adapt(call: Call<R>?): Any {
            return (wrapped!!.adapt(call) as Observable<*>).onErrorResumeNext(
                    Function { t -> Observable.error(t) })
        }

        override fun responseType(): Type {
            return wrapped!!.responseType()
        }

        fun convertToResponseException(throwable: Throwable): ResponseException {
            if (throwable is ResponseException) {
                return throwable
            }

            if (throwable is IOException) {
                return ResponseException.getNetworkError(throwable)
            }

            if (throwable is HttpException) {
                var response = throwable.response()
                if (response.errorBody() == null) {
                    return ResponseException.getHttpError(response)
                }
                try {
                    var errorData = response.errorBody().toString()
                    var errorResponse = deserializeErrorBody(errorData)
                    return if (errorResponse.isError()) {
                        ResponseException.getServerError(errorResponse)
                    }else{
                        ResponseException.getHttpError(response)
                    }
                } catch (e: IOException) {
                    Log.e(this.javaClass.simpleName, e.message)
                }
            }
            return  ResponseException.getUnexpectedError(throwable)
        }

        fun deserializeErrorBody(error: String): ErrorResponse {
            val gson = Gson()
            return gson.fromJson<ErrorResponse>(error, ErrorResponse::class.java)
        }
    }
}