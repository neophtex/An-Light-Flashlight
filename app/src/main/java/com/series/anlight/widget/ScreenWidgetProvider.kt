package com.series.anlight.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.series.anlight.R
import com.series.anlight.activities.ScreenActivity

/**
 * Implementation of App Widget functionality.
 */
class ScreenWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            RemoteViews(context.packageName, R.layout.screen_widget_provider).apply {
                updateAppWidget(context,appWidgetManager,appWidgetId)
            }
        }

        fun onEnabled(context: Context) {
            // Enter relevant functionality for when the first widget is created
        }

        fun onDisabled(context: Context) {
            // Enter relevant functionality for when the last widget is disabled
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager, appWidgetId: Int
    ) {
        val intent = Intent(context, ScreenActivity::class.java)

        val views = RemoteViews(context.packageName, R.layout.screen_widget_provider)

        val pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        views.setOnClickPendingIntent(R.id.appwidget_screen, pendingIntent)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}