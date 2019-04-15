
package com.cjc.sample.mediacodecexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cjc.sample.mediacodecexample.media.AudioPlayer
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        Log.e("PlayerActivity", "onCreate")

        val afd = resources.openRawResourceFd(R.raw.sample)
        val player = AudioPlayer()

        Observable.just(1).observeOn(Schedulers.io())
            .subscribe ({
                player.play(afd)

            }){
                Log.e("PlayerActivity", "$it")
            }

    }
}
