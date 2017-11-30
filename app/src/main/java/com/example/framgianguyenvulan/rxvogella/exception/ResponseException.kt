package com.example.framgianguyenvulan.rxvogella.exception

import android.content.Context
import com.example.framgianguyenvulan.rxvogella.R
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/30/17.
 */
class ResponseException : RuntimeException {

    var type: Type = Type.UNEXPECTED
    var error: ErrorResponse? = null
    var response: Response<*>? = null

    //http error
    constructor(type: Type, response: Response<*>) {
        this.type = type
        this.response = response
    }

    //server error
    constructor (type: Type, response: ErrorResponse) {
        this.type = type
        this.error = response
    }

    //throwable error
    constructor (type: Type, throwable: Throwable) : super(throwable.message, throwable) {
        this.type = type
    }

    companion object {
        fun getHttpError(response: Response<*>): ResponseException {
            return ResponseException(Type.HTTP, response)
        }

        fun getNetworkError(response: Response<*>): ResponseException {
            return ResponseException(Type.NETWORK, response)
        }

        fun getUnexpectedError(response: Response<*>): ResponseException {
            return ResponseException(Type.UNEXPECTED, response)
        }
    }

    enum class Type {
        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-2xx HTTP status code was received from the server.
         */
        HTTP,
        /**
         * A error server with code & mMessage
         */
        SERVER,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    fun getNetworkErrorMessage(context: Context, throwable: Throwable): String {
        if (throwable is UnknownHostException) {
            return context.getString(R.string.no_host_connection)
        }
        if (throwable is SocketTimeoutException) {
            return context.getString(R.string.io_exception)
        }
        return context.getString(R.string.no_network_connection)
    }
}