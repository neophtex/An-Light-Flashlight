package com.series.anlight

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.series.anlight.helper.PrefHelper
import com.series.anlight.widget.FlashWidgetUpdate


class Torch {
    private var cameraManager: CameraManager? = null
    private var context: Context? = null
    var speed = 0.0
    var thread: Thread? = null

    private lateinit var prefHelper: PrefHelper

    private var flashWidgetUpdate: FlashWidgetUpdate = FlashWidgetUpdate()

    fun flashLight(cameraManager: CameraManager?, context: Context?, prefHelper: PrefHelper) {
        this.cameraManager = cameraManager
        this.context = context
        this.prefHelper = prefHelper
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun flashLightOn() {
        try {
            val cameraId = cameraManager!!.cameraIdList[0]
            cameraManager!!.setTorchMode(cameraId, true)
        } catch (_: Exception) {
        }
        //update flashlight home widget

        context?.let { flashWidgetUpdate.updateWidgets(it.applicationContext) }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun flashLightOff() {
        try {
            val cameraId = cameraManager!!.cameraIdList[0]
            cameraManager!!.setTorchMode(cameraId, false)
        } catch (_: Exception) {
        }

        //update flashlight home widget
        context?.let { flashWidgetUpdate.updateWidgets(it.applicationContext) }
    }

    fun updateSpeed() {
        val sos_speed = 50.0
        speed = 1 / (sos_speed / 100);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun sos(morse: String, status: ImageView) {
        if(prefHelper.sos){
            status.setImageDrawable(context!!.getDrawable(R.drawable.ic_flashlight_off))
        }
        //sos thread
        thread = object : Thread() {
            override fun run() {
                try {
                    val delay = 1000
                    while (true) {
                        sosToFlash(morse,status)      //call sos function
                        sleep(delay.toLong())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        (this.thread as Thread).start()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun strobe(frequency: Long, status: ImageView) {
        if(prefHelper.stroboscope){
            status.setImageDrawable(context!!.getDrawable(R.drawable.ic_flashlight_off))
        }
        //strobe thread
        thread = object : Thread() {
            override fun run() {
                try {
                    while (true) {
                        if(prefHelper.flash&&!prefHelper.stroboscope) thread?.interrupt()
                        flashLightOn()
                        sleep(200)
                        flashLightOff()
                        sleep(200)
                        sleep(frequency)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        (this.thread as Thread).start()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun strobocancel() {
        //interrupt thread
        if (thread != null) {
            if (!thread!!.isInterrupted) {
                thread!!.interrupt();
                flashLightOff()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SuspiciousIndentation")
    fun sosToFlash(morse: String, status: ImageView) {
        if(prefHelper.sos){
            status.setImageDrawable(context!!.getDrawable(R.drawable.ic_flashlight_off))
        }
        //sos thread
        outerloop@for (x in morse.indices) {
            //interrupt thread when true
            if (prefHelper.flash&&!prefHelper.sos) thread?.interrupt()
                if (morse[x] == '.') {
                    flashLightOn()
                    Thread.sleep(200)
                    flashLightOff()
                    Thread.sleep(200)

                }
                if (morse[x] == '-') {
                    flashLightOn()
                    Thread.sleep(500)
                    flashLightOff()
                    Thread.sleep(500)
                }
                if (morse[x] == ' ') {
                    Thread.sleep(300)
                    Thread.sleep(300)
                }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun decodeToEnglist(morse: String, status: ImageView) {
        status.setColorFilter(context!!.getColor(R.color.flash_on))
        //morse code thread
        thread = object : Thread() {
            override fun run() {
                try {
                    val delay = 1000
                    while (true) {
                        for (x in morse.indices) {
                            if (morse[x] == '.') {
                                flashLightOn()
                                sleep(200)
                                flashLightOff()
                                sleep(200)

                            }
                            if (morse[x] == '-') {
                                flashLightOn()
                                sleep(500)
                                flashLightOff()
                                sleep(500)
                            }
                            if (morse[x] == ' ') {
                                sleep(300)
                                sleep(300)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        (this.thread as Thread).start()
    }
}