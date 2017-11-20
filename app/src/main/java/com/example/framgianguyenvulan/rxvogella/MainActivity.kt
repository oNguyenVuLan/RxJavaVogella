package com.example.framgianguyenvulan.rxvogella

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.example.framgianguyenvulan.rxvogella.adapter.StockDataAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*

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

    private fun initRecyclerView(): Unit {
        var layoutManager = LinearLayoutManager(this)
        stock_updates_recycler_view.layoutManager = layoutManager
        var list = ArrayList<String>()
        Observable.just("APPLE", "GOOGLE", "FACEBOOK")
                .subscribe { t: String ->
                    list.add(t)

                }
        stock_updates_recycler_view.adapter = StockDataAdapter(list)
    }
}


