package com.series.anlight.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent


class FlashWidgetUpdate {

    fun updateWidgets(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val widgetIds = manager.getAppWidgetIds(ComponentName(context, FlashWidgetProvider::class.java))

        if (widgetIds.isNotEmpty()) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
                .setClass(context, FlashWidgetProvider::class.java)
            context.sendBroadcast(intent)
        }
    }
}