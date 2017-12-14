package com.example.framgianguyenvulan.rxvogella.api

import com.example.framgianguyenvulan.rxvogella.model.WeatherData
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/22/17.
 */
interface WeatherService {
    @GET("weather?")
    fun getWeatherData(@Query("lat") lat: String, @Query("lon") lon: String,
                       @Query("appid") appid: String): Single<WeatherData>
    @GET("weather?")
    fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String,
                       @Query("appid") appid: String): Observable<WeatherData>
}