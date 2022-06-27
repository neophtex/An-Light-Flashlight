package com.series.anlight

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi


class Torch {
    //var flashLightStatus = false
    private var cameraManager: CameraManager? = null
    private var context: Context? = null
    var speed = 0.0
    var thread: Thread? = null

    fun flashLight(cameraManager: CameraManager?, context: Context?) {
        this.cameraManager = cameraManager
        this.context = context
        //flashLightStatus = false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun flashLightOn() {
        try {
            val cameraId = cameraManager!!.cameraIdList[0]
            cameraManager!!.setTorchMode(cameraId, true)
            //flashLightStatus = true
            //mark button
        } catch (e: Exception) {
            //Utils.showToast("Cannot turn flashlight on", Toast.LENGTH_SHORT, context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun flashLightOff() {
        try {
            val cameraId = cameraManager!!.cameraIdList[0]
            cameraManager!!.setTorchMode(cameraId, false)
            //flashLightStatus = false
            //mark button
        } catch (e: Exception) {
            //Utils.showToast("Cannot turn flashlight on", Toast.LENGTH_LONG, context)
        }
    }

    fun updateSpeed() {
        val sos_speed = 50.0
        speed = 1 / (sos_speed / 100);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun morseToFlash1(morse: String) {
        val handler = Handler()
        var delay = 5
        for (x in morse.indices) {
            if (morse[x] == '.') {
                handler.postDelayed({
                    flashLightOn()
                    //button.setTextColor(act.getColor(R.color.redAccent))
                    delay += (200 * speed).toLong().toInt()
                }, (delay.toLong()))
                handler.postDelayed({
                    flashLightOff()
                    //button.setTextColor(act.getColor(R.color.white))
                    delay += (200 * speed).toLong().toInt()
                }, (delay.toLong()))
            } else if (morse[x] == '-') {
                handler.postDelayed(Runnable {
                    flashLightOn()
                    //button.setTextColor(act.getColor(R.color.redPrimaryDark))
                    delay += (500 * speed).toLong().toInt()
                }, (delay.toLong()))
                handler.postDelayed(Runnable {
                    flashLightOff()
                    //button.setTextColor(act.getColor(R.color.white))
                    delay += (500 * speed).toLong().toInt()
                }, (delay.toLong()))
            } else if (morse[x] == ' ') {
                handler.postDelayed(
                    Runnable {
                        delay += (300 * speed).toLong().toInt()
                    }, (delay.toLong())
                )
                handler.postDelayed(
                    Runnable {
                        delay += (300 * speed).toLong().toInt()
                    }, (delay.toLong())
                )
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun strobe(morse: String, status: ImageView) {
        status.setImageDrawable(context!!.getDrawable(R.drawable.ic_flashlight_on))
        thread = object : Thread() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun run() {
                try {
                    val delay = 1000
                    while (true) {
                        if (thread!!.isInterrupted) break
                        morseToFlash(morse,status)
                        sleep(delay.toLong())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread!!.start()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun frequency(frequency: Long, status: ImageView) {
        status.setImageDrawable(context!!.getDrawable(R.drawable.ic_flashlight_on))
        thread = object : Thread() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun run() {
                try {
                    while (true) {
                        if (thread!!.isInterrupted) break
                        flashLightOn()
                        Thread.sleep(200)
                        flashLightOff()
                        Thread.sleep(200)
                        sleep(frequency)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread!!.start()
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun strobocancel() {
        if (!thread!!.isInterrupted) {
            thread!!.interrupt();
            flashLightOff()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
    fun morseToFlash(morse: String, status: ImageView) {
        status.setImageDrawable(context!!.getDrawable(R.drawable.ic_flashlight_on))
        outerloop@for (x in morse.indices) {
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
    @RequiresApi(Build.VERSION_CODES.M)
    fun decodeToEnglist(morse: String, status: ImageView) {
        status.setColorFilter(context!!.getColor(R.color.flash_on))
        thread = object : Thread() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun run() {
                try {
                    val delay = 1000
                    while (true) {
                        if (thread!!.isInterrupted) break
                        for (x in morse.indices) {
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
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread!!.start()
    }
}