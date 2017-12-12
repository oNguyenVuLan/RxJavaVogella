package com.example.framgianguyenvulan.rxvogella.model

import twitter4j.Status
import java.math.BigDecimal
import java.util.*

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/20/17.
 */
data class StockUpdate(var stockSymbol: String, var price: Double,var twitterStatus:String){
    companion object {
            fun create(weather:Weather):StockUpdate=
                    StockUpdate(weather.icon,weather.id.toDouble(),"")

        fun create(status: Status):StockUpdate=
                StockUpdate("",0.0,status.text)
    }
}