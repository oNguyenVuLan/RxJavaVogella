package com.example.framgianguyenvulan.rxvogella.model

import java.util.*

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/20/17.
 */
data class StockUpdate(var stockSymbol: String, var price: Double){
    companion object {
            fun create(weather:Weather):StockUpdate=
                    StockUpdate(weather.icon,weather.id.toDouble())
    }
}