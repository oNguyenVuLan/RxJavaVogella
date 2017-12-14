package com.example.framgianguyenvulan.rxvogella.exception

import android.text.TextUtils
import android.util.Log
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/30/17.
 */
abstract class ServiceSubscribe<T : BaseResponse> : DisposableObserver<T>() {
    var response: T? = null
    override fun onComplete() {
        if(!TextUtils.isEmpty(response!!.code)&&!TextUtils.isEmpty(response!!.message)){
            onError(ResponseException(ResponseException.Type.SERVER,
                    ErrorResponse(response!!.code,response!!.message)))
        }else if(response!=null){
            onSuccess(response!!)
        }else{
            Log.e("","")
        }
    }

    override fun onError(e: Throwable) {
        if(e is ResponseException){
            onError(e)
        }else{
            onError(ResponseException.getUnexpectedError(e))
        }
    }

    override fun onNext(t: T) {
        response = t
    }

    abstract fun onSuccess(item: T)

    abstract fun onError(error:ResponseException)
}