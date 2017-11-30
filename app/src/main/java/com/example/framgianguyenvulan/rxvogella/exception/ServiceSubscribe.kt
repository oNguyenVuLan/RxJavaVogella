package com.example.framgianguyenvulan.rxvogella.exception

import io.reactivex.observers.DisposableObserver
import retrofit2.Response
import java.io.Serializable

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/30/17.
 */
abstract class ServiceSubscribe<T:Serializable> : DisposableObserver<T>() {
    override fun onComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNext(t: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

     abstract fun onSuccess( item:T)

    abstract  fun onError()
}