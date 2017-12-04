package com.example.framgianguyenvulan.rxvogella

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import android.widget.Toast
import com.example.framgianguyenvulan.rxvogella.adapter.StockDataAdapter
import com.example.framgianguyenvulan.rxvogella.api.ServiceFactory
import com.example.framgianguyenvulan.rxvogella.api.WeatherService
import com.example.framgianguyenvulan.rxvogella.model.StockUpdate
import com.example.framgianguyenvulan.rxvogella.model.Weather
import com.example.framgianguyenvulan.rxvogella.model.WeatherData
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import twitter4j.*
import twitter4j.conf.Configuration
import twitter4j.conf.ConfigurationBuilder
import java.lang.Exception
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    var listdata = mutableListOf<StockUpdate>()
    var weather: Weather? = null
    var disposable: Disposable? = null
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
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
        createService()
        stock_updates_recycler_view.adapter = StockDataAdapter(listdata)

    }

    private fun createService() {
        var weatherService: WeatherService = ServiceFactory().create()
        //var data = weatherService.getWeatherData("35", "139", "b1b15e88fa797225412429c1c50c122a1")

        Observable.create { e: ObservableEmitter<Int> ->
            e.onNext(1)
            e.onNext(2)
            e.onComplete()
        }
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
                .map { t: WeatherData -> t.weather!! }
                .flatMap { t -> Observable.fromIterable(t) }
                //.doOnNext(this::saveWeather)
                .map { t -> StockUpdate.create(t) }
                .subscribe({ t ->
                    textView.text = t.stockSymbol
                    listdata.add(t)
                })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun getConfiguringTwitter(): Configuration {
        return ConfigurationBuilder()
                .setDebugEnabled(BuildConfig.DEBUG)
                .setOAuthConsumerKey(BuildConfig.CONSUMER_KEY)
                .setOAuthConsumerSecret(BuildConfig.CONSUMER_SECRET)
                .setOAuthAccessToken(BuildConfig.ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(BuildConfig.ACCESS_TOKEN_SECRET)
                .build()


    }

    fun observeTwitterStream(config: Configuration, filterQuery: FilterQuery): Observable<Status> {
        return Observable.create { e ->
            var twitterStream = TwitterStreamFactory(getConfiguringTwitter()).instance
            e.setCancellable { Schedulers.io().scheduleDirect { twitterStream.cleanUp() } }
            var listener: StatusListener = object : StatusListener {
                override fun onTrackLimitationNotice(numberOfLimitedStatuses: Int) {
                }

                override fun onStallWarning(warning: StallWarning) {
                }

                override fun onException(ex: Exception) {
                }

                override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {
                }

                override fun onStatus(status: Status) {
                    e.onNext(status)
                }

                override fun onScrubGeo(userId: Long, upToStatusId: Long) {
                }

            }
            twitterStream.addListener(listener)
            twitterStream.filter(filterQuery)
        }
    }

}


