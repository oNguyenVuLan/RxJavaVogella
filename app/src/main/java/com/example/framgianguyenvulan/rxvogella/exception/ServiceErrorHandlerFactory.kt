package com.example.framgianguyenvulan.rxvogella.exception

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/30/17.
 */
class ServiceErrorHandlerFactory : CallAdapter.Factory() {
    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}