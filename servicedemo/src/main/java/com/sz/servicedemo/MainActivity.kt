package com.sz.servicedemo

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

/**
 * startService 和 bindService 一起使用后，要销毁service，需要stopService 和 unbindService 一起用
 * service 执行线程是主线程，需要采用子线程解决ui阻塞问题
 * 在 manifest 申明为 remote service 后，service 在另一个进程里运行，需要使用 aidl 进行IPC
 */

class MainActivity : Activity(), View.OnClickListener {
    companion object {
        const val TAG = "MainActivity"
    }

    var serviceBinder:FooService.Binder? = null

    private var serviceConnection = object :ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "onServiceDisconnected  $name")
            serviceBinder = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "onServiceConnected  $name")
            serviceBinder = service as? FooService.Binder
        }

        override fun onBindingDied(name: ComponentName?) {
            Log.e(TAG, "onBindingDied  $name")

        }

        override fun onNullBinding(name: ComponentName?) {
            Log.e(TAG, "onNullBinding  $name")

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStarService.setOnClickListener(this)
        btnStopService.setOnClickListener(this)

        btnBindService.setOnClickListener(this)
        btnUnbindService.setOnClickListener(this)
        btnJob.setOnClickListener(this)
     }

    override fun onClick(v: View?) {
        when (v?.id) {
            btnStarService.id -> {
                val componentName = startService(Intent(this, FooService::class.java))
                Log.e(TAG, "$componentName")
            }

            btnStopService.id -> {
                val result = stopService(Intent(this, FooService::class.java))
                Log.e(TAG, "stopService : $result")

            }

            btnBindService.id -> {
                val binded = bindService(Intent(this, FooService::class.java),
                    serviceConnection, Context.BIND_AUTO_CREATE)
                Log.e(TAG, "bindService result:$binded")
            }
            btnUnbindService.id -> {
                /**
                 * 注意， unbindServce 不会调用 serviceConnection 的 onServiceDisconnected
                 * 只有 service 崩溃或者进程崩溃才会被调用，不能依赖它
                 */
                if (serviceBinder != null) {
                    serviceBinder = null
                    unbindService(serviceConnection)
                }
            }
            btnJob.id -> {
                serviceBinder?.doMyJob()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBinder != null) {
            serviceBinder = null
            unbindService(serviceConnection)
        }
    }
}
