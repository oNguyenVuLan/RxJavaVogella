package com.example.framgianguyenvulan.rxvogella

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.example.framgianguyenvulan.rxvogella.adapter.StockDataAdapter
import com.example.framgianguyenvulan.rxvogella.model.StockUpdate
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import io.reactivex.subjects.PublishSubject



class MainActivity : AppCompatActivity() {


    lateinit var textView: TextView

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
        var listdata = mutableListOf<StockUpdate>()
        Observable.just(StockUpdate("GOOGLE", 12.43, Date()),
                StockUpdate("APPL", 645.1, Date()),
                StockUpdate("TWTR", 1.43, Date()))
                .subscribe { t: StockUpdate ->
                    listdata.add(t)
                }
        stock_updates_recycler_view.adapter=StockDataAdapter(listdata)
    }
}


