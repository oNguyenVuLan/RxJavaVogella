package com.example.framgianguyenvulan.rxvogella

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TextView
import com.example.framgianguyenvulan.rxvogella.adapter.StockDataAdapter
import com.example.framgianguyenvulan.rxvogella.api.ServiceFactory
import com.example.framgianguyenvulan.rxvogella.api.WeatherService
import com.example.framgianguyenvulan.rxvogella.db.StorIOFactory
import com.example.framgianguyenvulan.rxvogella.model.StockUpdate
import com.example.framgianguyenvulan.rxvogella.model.Weather
import com.example.framgianguyenvulan.rxvogella.model.WeatherData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {


    lateinit var textView: TextView
    var listdata = mutableListOf<StockUpdate>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.hello_world_salute)
        Observable.just("Hello")
                .subscribe { t: String -> textView.text = t }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        var layoutManager = LinearLayoutManager(this)
        stock_updates_recycler_view.layoutManager = layoutManager

        /*
        Observable.just(StockUpdate("GOOGLE", 12.43, Date()),
                StockUpdate("APPL", 645.1, Date()),
                StockUpdate("TWTR", 1.43, Date()))
                .subscribe { t: StockUpdate ->
                    listdata.add(t)
                }
                */

        createService()
        stock_updates_recycler_view.adapter = StockDataAdapter(listdata)
    }

    private fun createService() {
        var weatherService: WeatherService = ServiceFactory().create()
        //var data = weatherService.getWeatherData("35", "139", "b1b15e88fa797225412429c1c50c122a1")

        Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap<WeatherData> {
                 it->weatherService.getWeatherData("35", "139", "b1b15e88fa797225412429c1c50c122a1")
                        .toObservable()
                }
                .subscribeOn(Schedulers.io())
                .map<List<Weather>> { t: WeatherData -> t.weather!! }
                .flatMap { t -> Observable.fromIterable(t) }
                //.doOnNext(this::saveWeather)
                .map {t -> StockUpdate.create(t)  }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    textView.text= t.stockSymbol
                    listdata.add(t)
                    })
    }

    private fun saveWeather(weather:Weather){
        StorIOFactory.get(this)?.put()
                ?.`object`(weather)
                ?.prepare()
                ?.asRxSingle()
                ?.subscribe()
    }
}


