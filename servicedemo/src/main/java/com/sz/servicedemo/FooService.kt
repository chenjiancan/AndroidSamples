package com.sz.servicedemo

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log

class FooService : Service() {
    companion object {
        const val TAG = "FooService"
    }

    inner class Binder(val service: FooService) : android.os.Binder() {
        fun doMyJob() {
            Log.e(TAG, "doMyJob")
//            Handler().postDelayed({
//                stopSelf()
//            }, 2000)
        }
    }

    override fun onBind(intent: Intent): IBinder? {

        Log.e(TAG, "onBind")

        return Binder(this)
     }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand startId: $startId")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")

        super.onCreate()

    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")

        super.onDestroy()

    }
}
