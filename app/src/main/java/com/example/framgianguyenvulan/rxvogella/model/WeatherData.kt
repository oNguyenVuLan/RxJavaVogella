package com.example.framgianguyenvulan.rxvogella.model

import com.example.framgianguyenvulan.rxvogella.exception.BaseResponse
import java.io.Serializable

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/22/17.
 */
class WeatherData(code: String, message: String) : BaseResponse(code, message) {
    var id: Int = 0
    var name: String = ""
    var cod: Int = 0
    var coord: Coord? = null
    var main: Main? = null
    var weather:List<Weather>?=null
    var wind: Wind? = null
}