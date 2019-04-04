package com.cjc.rxdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.reactivestreams.Publisher
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    data class Duration(val duration: Long, val timeUnit: TimeUnit)

    fun <T> Observable<T>.retry(
        predicate: (Throwable) -> Boolean,
        maxRetry: Long,
        delayBeforeRetry: Duration
    ): Observable<T> =
        retryWhen {
            Observables.zip(
                it.map {
                    if (predicate(it)) {
                        it
                    } else {
                        throw it
                    }
                },
                Observable.interval(delayBeforeRetry.duration, delayBeforeRetry.timeUnit)
            )
                .map {
                    if (it.second >= maxRetry)
                        throw it.first
                }


        }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method


        var i = 0
//        Observable.create<Int> {
//            while (i < 3) {
//
//                if (i == 1) {
//                    i++
//                    throw IOException()
//                } else {
//                    it.onNext(i)
//                    i++
//                }
//
//            }
//            it.onComplete()
//        }
////            Observable.just(1)
////            .retry(
////            predicate = {
////                it is IOException
////            },
////            maxRetry = 3,
////            delayBeforeRetry = Duration(100, TimeUnit.MILLISECONDS))
//            .retryWhen {
//                it.flatMap {
//                    if (it is IOException) {
//                        return@flatMap Observable.just(100).delay(5, TimeUnit.SECONDS)  // 触发重试
//                    } else {
//                        return@flatMap Observable.error<Any>(it)
//                    }
//                }
//
//            }
//            .observeOn(Schedulers.io()).subscribe({
//                //            if (i == 0) {
////                i++
////                throw IOException()
////            }
//                Log.e("test", "$it")
//            })
//            {
//                Log.e("test", "$it")
//            }.addTo(compositeDisposable)



        Observable.create<Int> {
            if (i < 4) {

                if (i <= 1) {
                    i++
                    throw IOException()
                } else {
                    it.onNext(i)
                    i++
                }

            }
            it.onComplete()
        }
            .retry(
                predicate = {
                    it is IOException
                },
                maxRetry = 3,
                delayBeforeRetry = Duration(2000, TimeUnit.MILLISECONDS)
            )

            .observeOn(Schedulers.io()).subscribe({
                //            if (i == 0) {
//                i++
//                throw IOException()
//            }
                Log.e("test", "$it")
            })
            {
                Log.e("test", "$it")
            }.addTo(compositeDisposable)


    }

    var i = 0
    private fun getMyTask(): io.reactivex.Single<String> {
        return io.reactivex.Single.fromCallable(Callable {
            Log.d("ERSEN", "Task Started!")
            val random = Random(System.currentTimeMillis())

            //                if(random.nextBoolean()){
            //                    return "WORK COMPLETED";
            //                }

            Log.e("test", "call $i times")
            if (i++ == 0) {

                Log.d("ERSEN", "Task Had An Error!")
                throw IOException()

            } else {
                if (random.nextBoolean()) {
                    return@Callable "WORK COMPLETED"
                }
                throw IOException()
            }
        })
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        getMyTask()
//            .retryWhen { throwableFlowable ->
//                throwableFlowable.flatMap { throwable ->
//                    if (throwable is IOException) {
//                        Flowable.just("ignored").delay(1, TimeUnit.SECONDS)
//                    } else Flowable.error<Any>(throwable)
//                }
//            }.subscribe { s -> Log.e("test1", s) }.addTo(compositeDisposable)
//    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}

