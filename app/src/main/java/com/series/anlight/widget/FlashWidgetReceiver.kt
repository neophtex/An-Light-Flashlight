package com.series.anlight.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.widget.RemoteViews
import com.series.anlight.R
import com.series.anlight.Torch
import com.series.anlight.helper.PrefHelper


class FlashWidgetReceiver : BroadcastReceiver() {
    private var cameraManager: CameraManager? = null
    private var cameraId: String = ""

    private var torch: Torch = Torch()
    private lateinit var prefHelper : PrefHelper

    private var flash = false
    //private var sos = false
    //private var stroboscope = false

    override fun onReceive(context: Context, intent: Intent) {

        prefHelper = PrefHelper(context)
        flash = prefHelper.flash
        //stroboscope = prefHelper.stroboscope
        //sos = prefHelper.sos

        val views = RemoteViews(context.packageName, R.layout.flash_widget_provider)

        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager!!.cameraIdList[0]

        torch.flashLight(cameraManager, context, prefHelper)

        if (!flash) {
            torch.flashLightOn()
            views.setImageViewResource(R.id.appwidget_flash, R.drawable.ic_flashlight_on)
            prefHelper.flash = true
            prefHelper.stroboscope = false
            prefHelper.sos = false
        }
        if (flash) {
            torch.flashLightOff()
            views.setImageViewResource(R.id.appwidget_flash, R.drawable.ic_flashlight_off)
            prefHelper.flash = false
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.updateAppWidget(ComponentName(context, FlashWidgetProvider::class.java), views)
    }
}