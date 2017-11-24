package com.example.framgianguyenvulan.rxvogella.model

import java.io.Serializable

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/22/17.
 */
class WeatherData : Serializable {
    var id: Int = 0
    var name: String = ""
    var cod: Int = 0
    var coord: Coord? = null
    var main: Main? = null
    var wind: Wind? = null
}