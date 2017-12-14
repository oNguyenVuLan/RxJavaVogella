package com.example.framgianguyenvulan.rxvogella

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.framgianguyenvulan.rxvogella.adapter.StockDataAdapter
import com.example.framgianguyenvulan.rxvogella.api.ServiceFactory
import com.example.framgianguyenvulan.rxvogella.api.WeatherService
import com.example.framgianguyenvulan.rxvogella.exception.ResponseException
import com.example.framgianguyenvulan.rxvogella.exception.ServiceSubscribe
import com.example.framgianguyenvulan.rxvogella.model.ErrorHandler
import com.example.framgianguyenvulan.rxvogella.model.StockUpdate
import com.example.framgianguyenvulan.rxvogella.model.Weather
import com.example.framgianguyenvulan.rxvogella.model.WeatherData
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    var listdata = mutableListOf<StockUpdate>()
    var weather: Weather? = null
    var disposable: Disposable? = null
    var disposable1: Observable<WeatherData>? = null
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.hello_world_salute)
        RxJavaPlugins.setErrorHandler(ErrorHandler.get())
        Observable.just("Hello")
                .subscribe { t: String -> textView.text = t }
        initRecyclerView()
        //testConcat()
        //testFlatMaybe()
        testServiceSubscrible()
    }

    private fun initRecyclerView() {
        var layoutManager = LinearLayoutManager(this)
        stock_updates_recycler_view.layoutManager = layoutManager
        stock_updates_recycler_view.adapter = StockDataAdapter(listdata)
    }

    private fun testFlatMaybe() {
        var weatherService: WeatherService = ServiceFactory().create()
        disposable = Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap<WeatherData> { it ->
                    weatherService.getWeatherData("35", "139", "b1b15e88fa797225412429c1c50c122a1")
                            .toObservable()
                }
                .doOnError { t: Throwable ->
                    Toast.makeText(this,
                            "We couldn't reach internet - falling back to local data",
                            Toast.LENGTH_SHORT)
                            .show()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<List<Weather>> { t: WeatherData -> t.weather!! }
                .flatMap { t -> Observable.fromIterable(t) }
                //.doOnNext(this::saveWeather)
                .map { t -> StockUpdate.create(t) }
                .flatMapMaybe { t: StockUpdate ->
                    Observable.fromArray(t)
                            .filter { t -> TextUtils.isEmpty(t.stockSymbol) }
                            .map { t -> t }
                            .firstElement()
                }
                .subscribe({ t -> Log.e("", "" + t.twitterStatus) })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun testServiceSubscrible() {
        var serviceSubscribe = object : ServiceSubscribe<WeatherData>() {
            override fun onSuccess(item: WeatherData) {
                Log.w("", Gson().toJson(item))
                var a: Float = item.wind!!.speed
                Toast.makeText(this@MainActivity, "2222", Toast.LENGTH_SHORT).show()
                textView.text = item.wind!!.speed.toString()
            }

            override fun onError(error: ResponseException) {
                Toast.makeText(this@MainActivity, "1 :" + error.error, Toast.LENGTH_SHORT).show()
            }

        }

        var weatherService: WeatherService = ServiceFactory().create()
        disposable1 = Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap<WeatherData> { it ->
                    // Log.e("","")
                    weatherService.getWeather("35", "139", "b1b15e88fa797225412429c1c50c122a1")

                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        compositeDisposable.add(disposable1!!.subscribeWith(serviceSubscribe))
    }
}


