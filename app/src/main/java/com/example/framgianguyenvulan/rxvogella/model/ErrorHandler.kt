package com.example.framgianguyenvulan.rxvogella.model

import android.util.Log

import io.reactivex.functions.Consumer

class ErrorHandler private constructor() : Consumer<Throwable> {

    @Throws(Exception::class)
    override fun accept(throwable: Throwable) {
        Log.e("APP", "Error on " + Thread.currentThread().name + ":", throwable)
    }

    companion object {

        private val INSTANCE = ErrorHandler()

        fun get(): ErrorHandler {
            return INSTANCE
        }
    }
}
