package com.example.framgianguyenvulan.rxvogella.exception

import android.text.TextUtils

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/30/17.
 */
class ErrorResponse(code: String, message: String) {
    var codeString = code
    fun isError(): Boolean {
        return TextUtils.isEmpty(codeString)
    }
}