package com.example.framgianguyenvulan.rxvogella;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by vulan on 12/12/2017.
 */

public class Test {
    void test() {
        Observable.zip(
                Observable.just("One", "Two", "Three")
                        .doOnDispose(() -> Log.e("just", "doOnDispose"))
                        .doOnTerminate(() -> Log.e("just", "doOnTerminate")),
                Observable.interval(1, TimeUnit.SECONDS)
                        .doOnDispose(() -> Log.e("interval", "doOnDispose"))
                        .doOnTerminate(() -> Log.e("interval", "doOnTerminate")),
                (number, interval) -> number + "-" + interval)
                .doOnDispose(() -> Log.e("zip", "doOnDispose"))
                .doOnTerminate(() -> Log.e("zip", "doOnTerminate")).subscribe(e -> Log.e(e,""));
    }
}
