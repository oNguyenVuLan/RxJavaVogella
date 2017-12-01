package com.example.framgianguyenvulan.rxvogella.exception

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/30/17.
 */
open class BaseResponse(code:String,message:String) :Serializable{
    @SerializedName("code")
    var code=code

    @SerializedName("message")
    var message=message
}