package com.series.anlight.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.series.anlight.R
import com.series.anlight.helper.PrefHelper

/**
 * Implementation of App Widget functionality.
 */
class FlashWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val prefHelper = PrefHelper(context)

    val intent = Intent(context, FlashWidgetReceiver::class.java)

    val views = RemoteViews(context.packageName, R.layout.flash_widget_provider)

    if (!prefHelper.flash) {
        views.setImageViewResource(R.id.appwidget_flash, R.drawable.ic_flashlight_off)
    }
    if (prefHelper.flash) {
        views.setImageViewResource(R.id.appwidget_flash, R.drawable.ic_flashlight_on)
    }

    val pendingIntent = PendingIntent.getBroadcast(context, 3, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

    views.setOnClickPendingIntent(R.id.appwidget_flash, pendingIntent);

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
