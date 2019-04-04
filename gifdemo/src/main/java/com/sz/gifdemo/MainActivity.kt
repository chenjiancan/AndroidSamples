package com.sz.gifdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val decoder = GifDecoder()
        decoder.init()

        val inputStream  = resources.openRawResource(R.raw.home)
        val code = decoder.read(inputStream, inputStream.available())
        Log.e("MainActivity", "$code")

        val count = decoder.getFrameCount()
        decoder.readContents()

        fun showFrame() {
            decoder.advance()
            val bmp = decoder.nextFrame ?.also {
                val bitmap = it
                image.setImageBitmap(bitmap)
            }
            val delay = decoder.nextDelay

            if(delay != -1 && bmp != null) {
                Handler().postDelayed({
                    showFrame()
                }, delay.toLong())
            }
        }

        Handler().post {

            showFrame()
        }




    }
}
