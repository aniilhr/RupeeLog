package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.local.AppDatabase
import com.example.util.CurrencyUtils
import com.example.util.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoneyTrackerWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        updateWidgetsInternal(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_UPDATE_WIDGET || intent.action == Intent.ACTION_DATE_CHANGED || intent.action == Intent.ACTION_TIME_CHANGED) {
            updateAllWidgets(context)
        }
    }

    companion object {
        const val ACTION_UPDATE_WIDGET = "com.example.ACTION_UPDATE_WIDGET"

        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, MoneyTrackerWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
                updateWidgetsInternal(context, appWidgetManager, appWidgetIds)
            }
        }

        private fun updateWidgetsInternal(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
        ) {
            val appContext = context.applicationContext
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val (start, end) = DateUtils.getCurrentMonthRange()
                    val db = AppDatabase.getInstance(appContext)
                    val monthlyTotal = db.expenseDao().getMonthlyTotalDirect(start, end) ?: 0.0
                    val formatted = CurrencyUtils.format(monthlyTotal)

                    for (widgetId in appWidgetIds) {
                        val views = RemoteViews(appContext.packageName, R.layout.widget_money_tracker)
                        views.setTextViewText(R.id.widget_amount, formatted)
                        views.setTextViewText(R.id.widget_subtitle, "Spent This Month")

                        // Click to launch MainActivity
                        val intent = Intent(appContext, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        val pendingIntent = PendingIntent.getActivity(
                            appContext,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

                        appWidgetManager.updateAppWidget(widgetId, views)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
