package com.cjc.rxdemo

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import java.util.concurrent.TimeUnit


/**
 * Created by ChenJiancan on 2019/4/1.
 */
class RxJava2UnitConnectableTest {
    @Test
    fun testConnect() {

        val connectableObservable = Observable
            //.range(1, 10000_000).sample(10, TimeUnit.MILLISECONDS)
            .create<Int> {
                for (i in 0 .. 100_000_000) {
                    it.onNext(i)
                }

                it.onComplete()
            }.sample(10, TimeUnit.MILLISECONDS)
            .publish()

        println(connectableObservable.javaClass)
        println("first")

        connectableObservable.subscribe {
            println("#A " + it)
        }

        println("second")

        connectableObservable.subscribe {
            println("#B " + it)
        }

        println("connect")

        connectableObservable.connect {
            println("connect...")
        }

        connectableObservable.subscribe {
            println("#C " + it)
        }

        connectableObservable.connect()
    }
    @Test
    fun testPublishSubject() {
        val subject = PublishSubject.create<Int>()

        subject.subscribe {
            println("#A " + it)
        }


        subject.subscribe {
            println("#B " + it)
        }

        subject.onNext(1)
        subject.onNext(2)
        subject.onComplete()

    }
}