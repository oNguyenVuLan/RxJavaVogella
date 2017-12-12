package com.example.framgianguyenvulan.rxvogella

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.framgianguyenvulan.rxvogella.adapter.StockDataAdapter
import com.example.framgianguyenvulan.rxvogella.api.ServiceFactory
import com.example.framgianguyenvulan.rxvogella.api.WeatherService
import com.example.framgianguyenvulan.rxvogella.model.ErrorHandler
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
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.plugins.RxJavaPlugins
import java.util.*
import java.util.concurrent.Callable
import android.util.Pair

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
        RxJavaPlugins.setErrorHandler(ErrorHandler.get())
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

        Observable.just("ID1", "ID2", "ID3")
                .map { id -> Observable.fromCallable(mockHttpRequest(id)) }
                .subscribe({ e -> Log.e("",e.toString()) })
        Observable.just("UserID1", "UserID2", "UserID3","UserID3")
                .map { id -> Pair.create(id, id + "-access-token") }
                .subscribe({ pair -> Log.e("subscribe-subscribe", pair.second) })
//        Observable.merge( Observable.interval(0, 5, TimeUnit.SECONDS)
//                .flatMap<WeatherData> { it ->
//                    weatherService.getWeatherData("35", "139", "b1b15e88fa797225412429c1c50c122a1")
//                            .toObservable()
//                }, observeTwitterStream(getConfiguringTwitter(),filterQuery))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnError (ErrorHandler.get())
//                .observeOn(Schedulers.io())

//        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun getConfiguringTwitter(): Configuration {
        return ConfigurationBuilder()
                .setDebugEnabled(BuildConfig.DEBUG)
//                .setOAuthConsumerKey(BuildConfig.CONSUMER_KEY)
//                .setOAuthConsumerSecret(BuildConfig.CONSUMER_SECRET)
//                .setOAuthAccessToken(BuildConfig.ACCESS_TOKEN)
//                .setOAuthAccessTokenSecret(BuildConfig.ACCESS_TOKEN_SECRET)
                .build()


    }

    val filterQuery=FilterQuery()
            .track("","Google")
            .language("en")
    fun observeTwitterStream(config: Configuration, filterQuery: FilterQuery): Observable<Status> {
        return Observable.create { e ->
            var twitterStream = TwitterStreamFactory(config).instance
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

    private fun mockHttpRequest(id: String): Callable<Date> {
        return Callable<Date> { Date() }
    }

}


