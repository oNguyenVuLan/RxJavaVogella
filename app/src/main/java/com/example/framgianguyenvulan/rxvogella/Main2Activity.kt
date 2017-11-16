package com.example.framgianguyenvulan.rxvogella

import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class Main2Activity : AppCompatActivity() {

    internal var colorListView: RecyclerView? = null
    internal var disposable: CompositeDisposable? = CompositeDisposable()
    var value = 0

    internal val serverDownloadObservable = Observable.create<Int> { emitter ->
        SystemClock.sleep(10000) // simulate delay
        emitter.onNext(5)
        emitter.onComplete()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_java_simple)
        val view = findViewById<View>(R.id.button)
        view.setOnClickListener { v ->
            v.isEnabled = false // disables the button until execution has finished
            val subscribe = serverDownloadObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe { integer ->
                updateTheUserInterface(integer) // this methods updates the ui
                v.isEnabled = true // enables it again
            }
            disposable!!.add(subscribe)
        }
    }

    private fun updateTheUserInterface(integer: Int) {
        val view = findViewById<View>(R.id.resultView) as TextView
        view.text = integer.toString()
    }

    override fun onStop() {
        super.onStop()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    fun onClick(view: View) {
        Toast.makeText(this, "Still active " + value++, Toast.LENGTH_SHORT).show()
    }
}