package com.example.framgianguyenvulan.rxvogella

import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RxJavaSimpleActivity : AppCompatActivity() {
    var compositeDisposable: CompositeDisposable? = CompositeDisposable()
    var value: Int = 0

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
            v.isEnabled = false
            val subscribe = serverDownloadObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe { it ->
                        updateTheUserInterface(it)
                        v.isEnabled = true
                    }
            compositeDisposable!!.add(subscribe)
        }
    }

    private fun updateTheUserInterface(integer: Int) {
        val view = findViewById<View>(R.id.resultView) as TextView
        view.text = integer.toString()
    }

    override fun onStop() {
        super.onStop()
        if (compositeDisposable != null && !compositeDisposable!!.isDisposed) {
            compositeDisposable!!.dispose()
        }
    }

    fun onClick(view: View) {
        Toast.makeText(this, "Still active " + value++, Toast.LENGTH_SHORT).show()
    }
}


